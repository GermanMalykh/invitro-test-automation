package tests.web.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "local");
        String path;
        
        if ("remote".equals(env)) {
            // Для удаленных запусков читаем из configs/web/remote.properties
            path = "configs/web/remote.properties";
        } else {
            // Для локальных запусков читаем из ресурсов
            path = "src/test/resources/configs/web/" + env + ".properties";
        }

        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config file: " + path, e);
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