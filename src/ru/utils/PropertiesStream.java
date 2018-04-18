package ru.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by makenshi on 4/12/18.
 */
public class PropertiesStream {

    private Properties callback;
    protected InputStream stream = null;
    protected InputStreamReader reader = null;

    public PropertiesStream(String path, String charset) {
        callback = new Properties();

        try {
            try {
                stream = new FileInputStream(new File(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                reader = new InputStreamReader(stream, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            callback = new Properties();
            try {
                callback.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getProperties() {
        return this.callback;
    }

}
