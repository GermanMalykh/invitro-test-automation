package tests.android.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "local");
        String path;
        
        if ("remote".equals(env)) {
            // Для удаленных запусков читаем из configs/mobile/remote.properties
            path = "configs/mobile/remote.properties";
        } else {
            // Для локальных запусков читаем из ресурсов
            path = "src/test/resources/configs/mobile/local.properties";
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
        
        // Обрабатываем переменные окружения в формате ${VAR_NAME}
        return resolveEnvironmentVariables(value);
    }
    
    private static String resolveEnvironmentVariables(String value) {
        if (value == null || value.isEmpty()) return value;
        
        // Простая замена переменных в формате ${VAR_NAME}
        String result = value;
        while (result.contains("${")) {
            int start = result.indexOf("${");
            int end = result.indexOf("}", start);
            if (end == -1) break; // Нет закрывающей скобки
            
            String varName = result.substring(start + 2, end);
            String envValue = System.getenv(varName);
            
            if (envValue == null) {
                // Если переменная окружения не найдена, оставляем как есть
                break;
            }
            
            result = result.substring(0, start) + envValue + result.substring(end + 1);
        }
        
        return result;
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