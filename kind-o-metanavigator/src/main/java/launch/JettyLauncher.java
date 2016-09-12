package launch;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyLauncher {

	private static Server server;

	public static void main(String[] args) throws URISyntaxException, MalformedURLException {

		server = new Server(8008);
		HandlerList a = new HandlerList();
		
		//static
		ResourceHandler resourceHandler= new ResourceHandler();
        resourceHandler.setResourceBase("webapps");
        resourceHandler.setDirectoriesListed(true);
		
        //webservices
		ResourceConfig config = new ResourceConfig();
		config.packages("services");
		ServletHolder servlet = new ServletHolder(new ServletContainer(config));
		ServletContextHandler context = new ServletContextHandler(server, "/server"); //context path
		context.addServlet(servlet, "/*"); //server/ws

		a.addHandler(resourceHandler); // index
		a.addHandler(context);
		server.setHandler(a);
		
        // Start the server
        try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
