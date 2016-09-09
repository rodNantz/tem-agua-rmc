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
	
}

