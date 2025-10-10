package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    //ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")
    //→ Automatically finds your config.properties inside the resources folder (no need for a path).
    //
    //No file path or slashes needed — it’s portable and works everywhere (Windows, Mac, Jenkins, Docker, etc.).

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("❌ config.properties not found in resources folder");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    //for numeric Data:

    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
