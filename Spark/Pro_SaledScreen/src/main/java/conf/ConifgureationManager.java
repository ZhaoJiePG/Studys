package conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConifgureationManager {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream resourceAsStream = ConifgureationManager.class.getClassLoader().getResourceAsStream("resources/my.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getSttringValue(String key) {
        return getProperty(key);
    }

    public static Integer getIntegerValue(String key) {
        Integer value;
        value = Integer.valueOf(getProperty(key));
        return value;
    }

    public static Boolean getBooleanValue(String key) {
        Boolean value;
        value = Boolean.valueOf(getProperty(key));
        return value;
    }


}
