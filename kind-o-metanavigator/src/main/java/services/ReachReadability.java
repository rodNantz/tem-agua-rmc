package services;

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

import com.google.gson.Gson;

import services.bean.ReadabilityResponse;

@Path("readapi")
public class ReachReadability {
	
	ArrayList<String> lista = new ArrayList<String>();
	Gson gson = new Gson();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getNames(){
		lista.add("read");
		lista.add("abi");
		lista.add("lity!");
		return lista;
	}
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String readabilityApi(String url){
		//https://readability.com/api/content/v1/parser
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
			e.printStackTrace();
			return "error";
		}
	}
}

