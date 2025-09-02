package tests.web.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.HashMap;

public class WebConfig {

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    static void setup() {

        String testType = System.getProperty("test.type");
        String env = System.getProperty("env", "local");

        DesiredCapabilities capabilities = new DesiredCapabilities();

        Assumptions.assumeTrue("web".equals(testType) || "all".equals(testType) || testType == null,
                "Этот тест должен запускаться только для Web тестов или всех тестов");

        if (env.equals("remote")) {
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.remote = ConfigReader.get("selenoid_url");
            Configuration.reopenBrowserOnFail = false;
        }

        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 60000;

        Configuration.browser = ConfigReader.get("browser_name");
        Configuration.browserVersion = ConfigReader.get("browser_version");
        Configuration.browserSize = ConfigReader.get("browser_size");

        Configuration.pageLoadStrategy = "eager";

        // Настройка русской локали для Chrome
        if (Configuration.browser.equals("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--lang=ru");
            chromeOptions.addArguments("--accept-lang=ru");
            chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

            // Устанавливаем русскую локаль в preferences
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("intl.accept_languages", "ru");
            prefs.put("profile.default_content_setting_values.notifications", 2);
            chromeOptions.setExperimentalOption("prefs", prefs);

            // Применяем ChromeOptions к capabilities
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        }
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        String env = System.getProperty("env", "local");

        Attach.screenshot();
        Attach.pageSource();
        Attach.browserConsoleLogs();

        if (env.equals("remote")) {
            Attach.addVideo();
        }

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();

        if (env.equals("remote")) {
            Selenide.closeWebDriver();
        }
    }

}
