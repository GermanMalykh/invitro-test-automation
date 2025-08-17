package tests.api.utils;

import tests.api.constants.ApiConfigConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AllureEnv {
    /**
     * Сгенерировать build/allure-results/environment.properties
     */
    public static void writeAllureEnvironment() {
        Properties props = new Properties();

        // System
        props.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        props.setProperty("Java", System.getProperty("java.version"));
        props.setProperty("Gradle", System.getProperty("Gradle", "unknown"));

        // API Configuration
        props.setProperty("API.BaseUrl", ApiConfigConstants.BASE_URL.getValue());
        props.setProperty("API.UserAgent", ApiConfigConstants.USER_AGENT.getValue());

        // Запись файла
        File out = new File("build/allure-results/environment.properties");
        //noinspection ResultOfMethodCallIgnored
        out.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(out)) {
            props.store(fos, "Allure Environment");
        } catch (IOException ignored) {
        }
    }
}
