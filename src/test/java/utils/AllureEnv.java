package utils;

import tests.api.constants.ApiConfigConstants;
import tests.web.config.ConfigReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllureEnv {
    /**
     * Сгенерировать build/allure-results/environment.properties
     */
    public static void writeAllureEnvironment() {
        Properties props = new Properties();

        // Test Type
        String testType = System.getProperty("test.type", "unknown");
        String environment = System.getProperty("env", "local");
        props.setProperty("Test.Type", testType);
        props.setProperty("Environment", environment);
        
        // Определяем конкретную задачу на основе тегов и окружения
        String taskName = determineTaskName(testType, environment);
        props.setProperty("Gradle.Task", taskName);

        if ("local".equals(environment)) {
            props.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            props.setProperty("Java", System.getProperty("java.version"));
            props.setProperty("Gradle", System.getProperty("Gradle", "unknown"));
        }

        if ("api".equals(testType) || "all".equals(testType)) {
            try {
                props.setProperty("API.BaseUrl", ApiConfigConstants.BASE_URL.getValue());
                props.setProperty("API.BaseUrl.Mobile", ApiConfigConstants.BASE_URL_MOBILE.getValue());
                props.setProperty("API.UserAgent", ApiConfigConstants.USER_AGENT.getValue());
                props.setProperty("API.UserAgent.Mobile", ApiConfigConstants.USER_AGENT_MOBILE.getValue());
            } catch (Exception e) {
                // Не записываем ничего, если API конфигурация недоступна
            }
        }

        if ("web".equals(testType) || "all".equals(testType)) {
            try {
                props.setProperty("Web.Browser", ConfigReader.get("browser_name"));
                props.setProperty("Web.Browser.Version", ConfigReader.get("browser_version"));
                props.setProperty("Web.Browser.Size", ConfigReader.get("browser_size"));

                // Маскируем чувствительные данные в URL
                String selenoidUrl = ConfigReader.get("selenoid_url");
                props.setProperty("Web.Selenoid.Url", maskSensitiveData(selenoidUrl));
            } catch (Exception e) {
                // Не записываем ничего, если Web конфигурация недоступна
            }
        }

        if ("android".equals(testType) || "all".equals(testType)) {
            try {
                props.setProperty("Android.Device", tests.android.config.ConfigReader.get("device"));
                props.setProperty("Android.OS.Version", tests.android.config.ConfigReader.get("os_version"));
                props.setProperty("Android.App.Package", tests.android.config.ConfigReader.get("appPackage"));
                props.setProperty("Android.App.Activity", tests.android.config.ConfigReader.get("appActivity"));
                props.setProperty("Android.Appium.Url", tests.android.config.ConfigReader.get("appiumUrl"));
            } catch (Exception e) {
                // Не записываем ничего, если Android конфигурация недоступна
            }
        }

        // Запись файла
        File out = new File("build/allure-results/environment.properties");
        //noinspection ResultOfMethodCallIgnored
        out.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(out)) {
            props.store(fos, "Allure Environment");
        } catch (IOException ignored) {
        }
    }

    /**
     * Определяет название Gradle задачи на основе типа тестов и окружения
     */
    private static String determineTaskName(String testType, String environment) {
        switch (testType) {
            case "api":
                return "api" + (environment.equals("local") ? "LocalTests" : "RemoteTests");
            case "android":
                return "android" + (environment.equals("local") ? "LocalTests" : "RemoteTests");
            case "web":
                return "web" + (environment.equals("local") ? "LocalTests" : "RemoteTests");
            case "all":
                return "all" + (environment.equals("local") ? "LocalTests" : "RemoteTests");
            default:
                return "unknown";
        }
    }

    /**
     * Маскирует чувствительные данные в URL (логин:пароль)
     */
    private static String maskSensitiveData(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }

        // Паттерн для поиска логина:пароль в URL
        Pattern pattern = Pattern.compile("(https?://)([^:]+):([^@]+)@(.+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String protocol = matcher.group(1);
            String host = matcher.group(4);
            return protocol + "***:***@" + host;
        }

        return url;
    }

}
