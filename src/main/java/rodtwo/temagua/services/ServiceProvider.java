package rodtwo.temagua.services;

import java.io.File;
import java.io.FileNotFoundException;
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
import rodtwo.temagua.util.MyLog;
import rodtwo.temagua.util.MyLog.Group;

// /server/ws
@Path("/ws")
public class ServiceProvider {

	Gson gson = new Gson();
	MyLog log = MyLog.getInstance();
	
	/**
	 * Scraping do site da Sanepar - rodízio em Curitiba e RM
	 * ex. chamada: /server/grupos-rodizio/2-SAIC 
	 * 
	 * @param url
	 * @return htmlString
	 */
	@GET
	@Path("/{end}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_HTML) 
	public String treatUrl(@PathParam("end") String input){
		// Grupo 2 SAIC - "área do Recalque Baixo do Reservatório Cajuru"
		
		input = input.replace('-',' '); 
		
		String newUrl = "http://site.sanepar.com.br/grupos-rodizio"; 		// tabela
		String html = "Sanepar - Rodízio RMC - Grupo: "+ input + "\n\n";
		
		List<String> formIdBlocklist = new ArrayList<>();
		formIdBlocklist.add("search-block-form");
		
		// JSOUP
		try {
			log.add(Group.INFO, "Using JSOUP...");
			
			Connection.Response resp = Jsoup.connect(newUrl).timeout(10*1000).execute();
			Document doc = resp.parse();
			//log.add(Group.INFO, "doc: "+ doc);
			
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
		
			// read text
			for (Element entityNumber : searchResults.select("table.views-table")) {
				log.add(Group.INFO, "searchresElement: " + entityNumber.text());
				int i = 1;
				for (Element entity : searchResults.select("span.data-rodizio")) {
					log.add(Group.INFO, "e" + i + ": " + (entity));
					boolean isIniRodizio = (i % 2 != 0); 
					if (isIniRodizio)
						html += "Início do rodízio: ";
					else
						html += "Previsão de volta da água: ";
					html += entity.text() + 
							(isIniRodizio ? "\n" : "\n\n");
					i++;
				}
				break; // do only once
			}
			
		} catch(HttpStatusException httpExc){
			log.add(Group.ERROR, httpExc.getMessage());
			return Enums.HTTP_ERROR.getProp();
		} catch(SocketTimeoutException timeoutExc){
			log.add(Group.ERROR, timeoutExc.getMessage());
			return Enums.TIMEOUT_ERROR.getProp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.add(Group.ERROR, e.getMessage());
			log.add(Group.ERROR, new String[]{
					e.getMessage(), 
					Calendar.getInstance().getTime().toString()}
			);
			e.printStackTrace();
		}
		
		log.add(Group.INFO, html);
		//source returned
		return html;
	}
	

	
	
}
