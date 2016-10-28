package services.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertySource {
	public static Properties props = new Properties();
	static {
		File propertiesFile = new File("resources/metanav.properties");
		try {
			props.load(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}