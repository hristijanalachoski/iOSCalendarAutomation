package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream stream = ConfigReader.class.getClassLoader()
                .getResourceAsStream("configs/config.properties")) {
            if (stream == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getProperty(String property) {
        return properties.getProperty(property);
    }
}
