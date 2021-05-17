package rodtwo.temagua.launch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import rodtwo.temagua.services.ServiceProvider;
import rodtwo.temagua.services.bean.PropertySource;
import rodtwo.temagua.util.MyLog;
import rodtwo.temagua.util.MyLog.Group;

public class JettyLauncher {

	private static Server server;
	private static int port = 8080;
	private static MyLog log = MyLog.getInstance();

	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		
		String svrPort = System.getenv("PORT");
		port = svrPort != null ? Integer.parseInt(svrPort) 
								  : Integer.parseInt(PropertySource.props.getProperty("temagua.launch.port"));
		System.out.println("port: "+ port);
		
		server = new Server(port);
		HandlerList a = new HandlerList();
		
		//static content
		ResourceHandler resourceHandler= new ResourceHandler();
        resourceHandler.setResourceBase("webapps");
        resourceHandler.setDirectoriesListed(true);
		
        //webservices
		ResourceConfig config = new ResourceConfig();
		System.err.println(ServiceProvider.class.getPackage() + " added");
		config.packages(ServiceProvider.class.getPackage().toString());
		
		ServletHolder servlet = new ServletHolder(new ServletContainer(config));
		ServletContextHandler context = new ServletContextHandler(server, "/server"); //context path
		context.addServlet(servlet, "/*"); //server/ws

		a.addHandler(resourceHandler); // index
		a.addHandler(context);
		server.setHandler(a);
		
        // Start the server
        try {
        	log.add(Group.INFO, "@localhost:" + port);
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
