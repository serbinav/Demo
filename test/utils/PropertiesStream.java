package utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by makenshi on 4/12/18.
 */
public class PropertiesStream {

    private final Properties properties = new Properties();

    public PropertiesStream(String path, String charset) {

        try (InputStream stream = new FileInputStream(new File(path));
             InputStreamReader reader = new InputStreamReader(stream, charset)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

}
