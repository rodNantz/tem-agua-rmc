package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import javassist.bytecode.analysis.ControlFlow.Node;
import services.bean.ReadabilityResponse;
import test.MyLog;
import test.MyLog.Group;

// /server/ws
@Path("/ws")
public class HtmlProvider {

	Gson gson = new Gson();
	static MyLog log = new MyLog();
	/**
	 * Receives a URL and retrieves its HTML source treated by Readability API.
	 * localhost:8008/server/ws/example.com
	 * @param url
	 * @return htmlString
	 */
	@GET
	@Path("/{url}")
	@Produces("text/plain")
	public String readabilityCall(@PathParam("url") String url){
		
		log.add(Group.INFO, "entered readabilityCall");
		String newUrl = "";
		String html = ""; 
		
		try {
			if( (!url.startsWith("http://")) && (!url.startsWith("https://")) ){			
				newUrl = "http://" + url;
			} else {
				newUrl = url;
			}
			url = null;
			
			String token = Enums.TOKEN_RAPI.getProp();
			String endpoint = Enums.ENDPOINT_RAPI.getProp();
			String finalEndpoint = endpoint
							+ "?url=" + newUrl
							+ "&token=" + token;
			
			WebTarget caller = ClientBuilder.newClient().target(finalEndpoint);
			Response response = caller
								.request()
								.get(Response.class);
			
			String objectStr = response.readEntity(String.class);
			ReadabilityResponse respObj = gson.fromJson(objectStr, ReadabilityResponse.class);
			
			if (respObj.getContent() != null){
				html = Processa(respObj.getContent());
				// successful
				return html;	
			} else {
				log.add(Group.ERROR, "@ERR: URL not found");
				return "@ERR: URL not found";
			}
			
		} catch (Exception e){
			log.add(Group.ERROR, e.getMessage());
			e.printStackTrace();
			return "@ERR: " + e.getMessage();
		}
		
	}

	
	/**
	 * Receives a URL and retrieves its HTML source, treated.
	 * 
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
			url = null;
			
		
		// JSOUP
		try {
			log.add(Group.INFO, "Using JSOUP...");
			Document doc = null;
			doc = Jsoup.connect(newUrl).timeout(10*1000).get();
			
			//doc.select("p").tagName("h4");
			
			html = Processa(doc);
			
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
	
	
	/**
	 * used by the services
	 * 
	 * @param rawHtml
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String Processa(String rawHtml) throws FileNotFoundException{
		Document doc = Jsoup.parse(rawHtml);
		return Processa(doc);
	}
	
	/**
	 * used by the services
	 * 
	 * @param doc
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String Processa(Document doc) throws FileNotFoundException{

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
		Elements linkElements = doc.select("script");
		for (Element element : linkElements) {
			element.attr("src", element.absUrl("abs:src"));
			element.addClass(Enums.LINK_CLASS.getProp());
		}
		
		
		doc.body().attr("ng-app", "metaNav");
		doc.body().attr("ng-controller","MainController");
		
		doc.body().id();
		
		//jquery
		doc.head().append("<script src=\"https://code.jquery.com/jquery-3.1.0.min.js\"></script>");
		
		String jQueryClick = new Scanner(new File("resources/jsSrc/jQueryCLick")).useDelimiter("\\Z").next();
		doc.body().append(jQueryClick);
		
		//angular
		doc.head().append("<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>");
		
		String angularController = new Scanner(new File("resources/jsSrc/angularController")).useDelimiter("\\Z").next();
		doc.head().append(angularController);
		
		return doc.html();
	}
	
	
}
