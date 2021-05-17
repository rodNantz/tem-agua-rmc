package rodtwo.temagua.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import javassist.bytecode.analysis.ControlFlow.Node;
import rodtwo.temagua.services.*;
import rodtwo.temagua.services.bean.HtmlStringBuilderHelper;
import rodtwo.temagua.services.bean.PeriodoRodizio;
import rodtwo.temagua.util.MyLog;
import rodtwo.temagua.util.MyLog.Group;

// /server/rodizio
@Path("/rodizio")
public class ServiceProvider {

	Gson gson = new Gson();
	MyLog log = MyLog.getInstance();
	static final String newUrl = "http://site.sanepar.com.br/grupos-rodizio";
	
	@GET
	@Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8") 
	public String endpointTest() {
		return "Test";
	}
	
	/**
	 * Scraping do site da Sanepar - rodízio em Curitiba e RM
	 * ex. chamada: /server/rodizio/2-SAIC 
	 * 
	 * @param url
	 * @return htmlString
	 */
	
	@GET
	@Path("/html/{end}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_HTML +"; charset=UTF-8") 
	public String getContentAsHTML(@PathParam("end") String input) {
		HtmlStringBuilderHelper html = new HtmlStringBuilderHelper();
		getHtmlHead(html);
		html.appendLn( "<div id=\"content\">" );
		html.appendLn( "Sanepar - Rodízio RMC - Grupo: "+ input + "<br/><br/>" );
		Response res = callPageScraping(input);
		
		// checar se contém lista
		if (res.hasEntity() && res.getEntity() instanceof List) { 
			List<?> rodizios = (List<?>) res.getEntity();
			for(Object o : rodizios) {
				PeriodoRodizio rod = (PeriodoRodizio) o;
				html.appendLn( "Início do rodízio: "+ rod.hrInicio + "<br/>" );
				html.appendLn( "Previsão de volta da água: "+ rod.hrFim + "<br/><br/>" );
			}
			if (rodizios.isEmpty())
				html.appendLn( "Grupo não encontrado." );
		} else {
			html.appendLn( "Erro "+ res.getStatus() + 
						(res.hasEntity() ? " - "+ res.getEntity() : "")
					   );
		}
		
		// fechar html
		html.appendLn( "</div>" );
		getHtmlEnd(html);
		
		return html.toString();
	}
	
	@GET
	@Path("/json/{end}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getContentAsJSON(@PathParam("end") String input){
		// TODO
		return "TODO";
	}
	
	private Response callPageScraping(String input){
		input = input.replace('-',' ');
		try {
			Connection.Response resp = Jsoup.connect(newUrl).timeout(10*1000).execute();
			List<PeriodoRodizio> rodizios = extrairRodizio(resp, input);

			//source returned
			return Response
				      .status(Status.OK)
				      .entity(rodizios)
				      .build();
			
		} catch(HttpStatusException httpExc){
			log.add(Group.ERROR, httpExc.getMessage());
			return Response
				      .status(httpExc.getStatusCode())
				      .entity(httpExc)
				      .build();
		} catch(SocketTimeoutException timeoutExc){
			log.add(Group.ERROR, timeoutExc.getMessage());
			return Response
				      .status(Status.GATEWAY_TIMEOUT)
				      .entity(timeoutExc)
				      .build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.add(Group.ERROR, e.getMessage());
			log.add(Group.ERROR, new String[]{
					e.getMessage(), 
					Calendar.getInstance().getTime().toString()}
			);
			e.printStackTrace();
			return Response
				      .status(500)
				      .entity(e)
				      .build();
		}
	}
	
	private List<PeriodoRodizio> extrairRodizio(Connection.Response resp, String input) throws IOException{
		// Grupo 2 SAIC - "área do Recalque Baixo do Reservatório Cajuru"
		Document doc = resp.parse();
		
		// find form
		FormElement form = null;
		for (Element potentialForm : doc.select("form")) {
			if (potentialForm.id().equals("views-exposed-form-tabela-grupo-rodizio-page-1"))
				form = (FormElement) potentialForm;
		}
		log.add(Group.INFO, "form: "+ form);
					
		Element textBox = form.select("input#edit-grupo").first();
		textBox.val(input);
		
		// ** Submit the form
		Document searchResults = form.submit().cookies(resp.cookies()).get();
	
		List<PeriodoRodizio> rodizios = new ArrayList<>();
		// read text
		for (Element entityNumber : searchResults.select("table.views-table")) {
			log.add(Group.INFO, "searchresElement: " + entityNumber.text());
			int i = 1;
			PeriodoRodizio rod = null;
			for (Element entity : searchResults.select("span.data-rodizio")) {
				log.add(Group.INFO, "e" + i + ": " + (entity));
				boolean isIniRodizio = (i % 2 != 0); 
				if (isIniRodizio) {
					rod = new PeriodoRodizio();
					rod.hrInicio = entity.text();
				} else {
					rod.hrFim = entity.text();
					rodizios.add(rod);
				}
				i++;
			}
			break; // do only once
		}
		
		return rodizios;
	}
	

	private void getHtmlHead(HtmlStringBuilderHelper sb) {
		sb.appendLn( "<!DOCTYPE html>" );
		sb.appendLn( "<html>" );
		sb.appendLn( "	<head>" );
		sb.appendLn( "		<meta charset=\"utf-8\"/>" );
		sb.appendLn( "	</head>" );
		sb.appendLn( "	<body>" );
	}
	
	private void getHtmlEnd(HtmlStringBuilderHelper sb) {
		sb.appendLn( "	</body>" );
		sb.appendLn( "</html>" );
	}
	
}
