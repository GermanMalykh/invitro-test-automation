package tests.android.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assumptions;
import tests.android.drivers.LocalMobileDriver;
import tests.android.drivers.RemoteMobileDriver;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sessionId;

public class AndroidConfig {

    @BeforeAll
    public static void setup() throws Exception {
        // Проверяем, что это действительно Android тест
        String testType = System.getProperty("test.type");

        // Android тесты должны запускаться только в android или all режиме
        // НЕ должны запускаться в web режиме
        if ("web".equals(testType)) {
            throw new RuntimeException("Android тесты не могут запускаться в Web окружении. " +
                    "Используйте ./gradlew android для запуска только Android тестов или ./gradlew allTests для всех тестов.");
        }

        Assumptions.assumeTrue("android".equals(testType) || "all".equals(testType) || testType == null,
                "Этот тест должен запускаться только для Android тестов или всех тестов");

        // Читаем окружение из системного свойства
        String env = System.getProperty("env", "local");

        switch (env) {
            case "remote":
                Configuration.browser = RemoteMobileDriver.class.getName();
                // Устанавливаем разумный timeout для Android тестов
                Configuration.pageLoadTimeout = 30000;
                break;
            case "local":
                Configuration.browser = LocalMobileDriver.class.getName();
                break;
            default:
                throw new Exception("Unrecognised env: " + env);
        }

        Configuration.browserSize = null;

        // Отключаем веб-функции Selenide для нативных приложений
        Configuration.remoteReadTimeout = 60000;
        Configuration.remoteConnectionTimeout = 60000;
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        open();
    }

    @AfterEach
    void addAttachments() {
        String sessionId = sessionId().toString();
        String env = System.getProperty("env", "local");

        if (env.equals("local")) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
        }
        closeWebDriver();
        if (env.equals("remote")) {
            Attach.getBrowserstackAttachments(sessionId);
        }
    }
}
