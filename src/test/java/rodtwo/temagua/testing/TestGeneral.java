package rodtwo.temagua.testing;

import static org.junit.Assert.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import rodtwo.temagua.services.Enums;
import rodtwo.temagua.services.bean.ReadabilityResponse;

public class TestGeneral {

	Gson gson = new Gson();
	
	@Test
	public void test() {
		try {
			// TODO
			Assert.assertTrue(true);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
}
