package rodtwo.temagua.services.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertySource {
	public static Properties props = new Properties();
	static {
		File propertiesFile = new File("resources/temagua.properties");
		try {
			props.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException nfe) {
			System.err.println("File not found: "+ propertiesFile);
			System.err.println(new File(".").getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}