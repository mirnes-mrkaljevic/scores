package ba.tiket.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Configuration {

	private static String getProperty(String propertyName) {
		Logger logger = LogManager.getLogger(Configuration.class);
		Properties properties = new Properties();
		try {
			properties.load(Configuration.class
					.getResourceAsStream("/config.properties"));
			return properties.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}

	}

	public static int getSchedulerTime() {

		return Integer.parseInt(getProperty("timerMillisec"));
	}
	
	public static String getConnectionString(){
	
		return getProperty("connectionString");
	}
	
	public static String getUsername()
	{
		return getProperty("username");
	}
	
	public static String getPassword()
	{
		return getProperty("password");
	}
	
	
}
