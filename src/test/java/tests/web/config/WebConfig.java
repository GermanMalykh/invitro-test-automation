package tests.web.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.AllureEnv;

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
        validateTestType(testType);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (env.equals("remote")) {
            setupRemoteEnvironment(capabilities);
        }
        setupBasicConfiguration();
        setupBrowserConfiguration(capabilities);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        String env = System.getProperty("env", "local");

        // Проверяем, что драйвер существует перед попыткой сделать скриншот
        if (Selenide.webdriver().driver().hasWebDriverStarted()) {
            Attach.screenshot();
            Attach.pageSource();
            if (!Configuration.browser.equals("firefox")) {
                Attach.browserConsoleLogs();
            }
            if (env.equals("remote")) {
                Attach.addVideo();
            }
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
        }

    }

    @AfterAll
    static void afterAll() {
        AllureEnv.writeAllureEnvironment();
    }

    private static void validateTestType(String testType) {
        Assumptions.assumeTrue("web".equals(testType) || "all".equals(testType) || testType == null,
                "Этот тест должен запускаться только для Web тестов или всех тестов");
    }

    // Настройка удаленного окружения
    private static void setupRemoteEnvironment(DesiredCapabilities capabilities) {
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));

        Configuration.remote = ConfigReader.get("selenoid_url");
        Configuration.reopenBrowserOnFail = false;
    }

    // Базовая конфигурация
    private static void setupBasicConfiguration() {
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 60000;

        Configuration.browser = ConfigReader.get("browser_name");
        Configuration.browserVersion = ConfigReader.get("browser_version");
        Configuration.browserSize = ConfigReader.get("browser_size");
        Configuration.pageLoadStrategy = "eager";
    }

    // Настройка браузера
    private static void setupBrowserConfiguration(DesiredCapabilities capabilities) {
        if (Configuration.browser.equals("chrome")) {
            setupChromeBrowser(capabilities);
        } else if (Configuration.browser.equals("firefox")) {
            setupFirefoxBrowser(capabilities);
        }
    }

    private static void setupChromeBrowser(DesiredCapabilities capabilities) {
        ChromeOptions chromeOptions = new ChromeOptions();

        // Аргументы командной строки
        chromeOptions.addArguments(
                "--lang=ru",
                "--accept-lang=ru",
                "--disable-blink-features=AutomationControlled");

        // Настройки предпочтений
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "ru");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);

        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
    }

    private static void setupFirefoxBrowser(DesiredCapabilities capabilities) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        // Настройки локали и уведомлений
        firefoxOptions.addPreference("intl.accept_languages", "ru");
        firefoxOptions.addPreference("general.useragent.locale", "ru");
        firefoxOptions.addPreference("dom.webnotifications.enabled", false);

        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
    }
}
