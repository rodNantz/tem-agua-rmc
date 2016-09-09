package services;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import services.bean.ReadabilityResponse;

@Path("")
public class HtmlProvider {

	ArrayList<String> lista = new ArrayList<String>();
	Gson gson = new Gson();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getWorking(){
		lista.add("yes,");
		lista.add("it");
		lista.add("works!");
		return lista;
	}

	/**
	 * Receives a URL and retrieves its HTML source, possibly treated.
	 * @param url
	 * @return htmlString
	 */
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String treatUrl(String url){
		
		String html = "";
		String newUrl = "";
		
			System.setProperty("http.proxyHost", "proxy.cinq.com.br");
			System.setProperty("http.proxyPort", "3128");
			
			if( (!url.startsWith("http://")) && (!url.startsWith("https://")) ){			
				newUrl = "http://" + url;
			} else {
				newUrl = url;
			}
		
		// first, trying with Readability API
		try {
			String token = Enums.TOKEN_RAPI.getProp();
			String endpoint = Enums.ENDPOINT_RAPI.getProp();
			String finalEndpoint = endpoint
							+ "?url=" + url
							+ "&token=" + token;
			WebTarget caller = ClientBuilder.newClient().target(finalEndpoint);
			Response response = caller
								.request()
								.get(Response.class);
			
			String objectStr = response.readEntity(String.class);
			ReadabilityResponse respObj = gson.fromJson(objectStr, ReadabilityResponse.class);
			return respObj.getContent();
			
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());	
			e.printStackTrace();
		}
		
		// If there was a error, trying with JSOUP
		
		try {
			Document doc = null;
			doc = Jsoup.connect(newUrl).timeout(10*1000).get();
			
			//doc.select("p").tagName("h4");
			
			String html = 
			
		} catch(HttpStatusException httpExc){
			return Enums.HTTP_ERROR.getProp();
		} catch(SocketTimeoutException timeoutExc){
			return Enums.TIMEOUT_ERROR.getProp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return html;
	}
	
	public static String Processa(String rawHtml){

		Document doc = Jsoup.parse(rawHtml);

		//doc.select("p").tagName("h4");
		
		//links
		Elements aHrefElements = doc.select("a");
		for (Element element : aHrefElements) {
		    element.attr("href", element.attr("abs:href"));
		    element.addClass(Enums.LINK_CLASS.getProp());
		}      
		
		Elements linkHrefElements = doc.select("link");
		for (Element element : linkHrefElements) {
		    element.attr("href", element.attr("abs:href"));
		    element.addClass(Enums.LINK_CLASS.getProp());
		}      
		
		//images
		Elements imgElements = doc.select("img");
		for (Element element : imgElements) {
		    element.attr("src", element.attr("abs:src"));
		    element.addClass(Enums.LINK_CLASS.getProp());
		}
		
		//scripts
		Elements linkElements = doc.head().select("script");
		for (Element element : linkElements) {
			element.attr("src", element.absUrl("abs:src"));
			element.addClass(Enums.LINK_CLASS.getProp());
		}
		
		return doc.html();
	}
	
	
}
