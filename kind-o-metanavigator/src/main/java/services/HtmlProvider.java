package services;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Path("")
public class HtmlProvider {

	ArrayList<String> lista = new ArrayList<String>();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getNames(){
		lista.add("yes,");
		lista.add("it");
		lista.add("works!");
		return lista;
	}
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String treatUrl(String url){
		Document doc = null;
		String html = "";
		String newUrl = "";
		try {
			
			System.setProperty("http.proxyHost", "proxy.cinq.com.br");
			System.setProperty("http.proxyPort", "3128");
			
			if( (!url.startsWith("http://")) && (!url.startsWith("https://")) ){			
				newUrl = "http://" + url;
			} else {
				newUrl = url;
			}
			
			doc = Jsoup.connect(newUrl).timeout(10*1000).get();
			
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
			
			html = doc.html();
			
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
	
}
