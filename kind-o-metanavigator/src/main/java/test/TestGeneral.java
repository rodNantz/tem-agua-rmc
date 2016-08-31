package test;

import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

public class TestGeneral {

	@Test
	public void test() {
		try {
			WebTarget target = ClientBuilder.newClient().target("http://localhost:8008/server/ws"); 
			Response response = target
					.request(MediaType.TEXT_PLAIN)
					.post(Entity.entity("http://wikipedia.org", MediaType.TEXT_PLAIN_TYPE));
			String html = response.readEntity(String.class);
			System.out.println( html.substring(0, 10) 
								+ "[...]" 
								+ html.substring(html.length() - 8,html.length())
								);
			Assert.assertNotNull(html);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}

}
