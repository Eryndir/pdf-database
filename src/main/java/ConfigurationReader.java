
import java.io.*;
import java.util.Properties;

public class ConfigurationReader {

  private static Properties readProperties() throws FileNotFoundException {
    Properties properties = new Properties();
    String fileName = "config.properties";
    File file = new File(fileName);

    InputStream is = new FileInputStream(file);
    try {
      properties.load(is);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return properties;
  }

  public static String getProperty(String key) throws FileNotFoundException {
    Properties properties = readProperties();
    return properties.getProperty(key);
  }
}
