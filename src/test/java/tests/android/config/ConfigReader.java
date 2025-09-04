package tests.android.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "local");
        
        // Пробуем разные пути для поиска файла
        String[] possiblePaths = {
            "src/test/resources/configs/mobile/" + env + ".properties",
            "configs/mobile/" + env + ".properties",
            System.getProperty("user.dir") + "/src/test/resources/configs/mobile/" + env + ".properties"
        };
        
        boolean loaded = false;
        Exception lastException = null;
        
        for (String path : possiblePaths) {
            try {
                try (FileInputStream fis = new FileInputStream(path)) {
                    props.load(fis);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                lastException = e;
                // Попробуем через ClassLoader
                try {
                    var inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(path);
                    if (inputStream != null) {
                        props.load(inputStream);
                        loaded = true;
                        break;
                    }
                } catch (IOException e2) {
                    lastException = e2;
                }
            }
        }
        
        if (!loaded) {
            throw new RuntimeException("Cannot load config file. Tried paths: " + String.join(", ", possiblePaths), lastException);
        }
    }

    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null)
            throw new RuntimeException("Missing config key: " + key);
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    public static String getEnv() {
        return System.getProperty("env", "local");
    }
}