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
        System.out.println("✓ Allure listener добавлен");
    }

    @BeforeAll
    static void setup() {
        String testType = System.getProperty("test.type");
        String env = System.getProperty("env", "local");
        validateTestType(testType);
        
        System.out.println("=== НАСТРОЙКА WEB КОНФИГУРАЦИИ ===");
        System.out.println("Окружение: " + env);
        System.out.println("Тип тестов: " + testType);
        
        // Отключаем verbose логирование Chrome для удаленной среды
        if (env.equals("remote")) {
            System.setProperty("webdriver.chrome.silentOutput", "true");
            System.setProperty("webdriver.chrome.verboseLogging", "false");
            System.out.println("✓ Отключено verbose логирование Chrome для удаленной среды");
        }
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (env.equals("remote")) {
            setupRemoteEnvironment(capabilities);
        }
        setupBasicConfiguration();
        setupBrowserConfiguration(capabilities);
        Configuration.browserCapabilities = capabilities;
        
        System.out.println("=== КОНФИГУРАЦИЯ ЗАВЕРШЕНА ===");
    }

    @AfterEach
    void addAttachments() {
        String env = System.getProperty("env", "local");
        
        System.out.println("--- Завершение теста ---");
        System.out.println("Окружение: " + env);
        
        // Проверяем, что драйвер существует перед попыткой сделать скриншот
        if (Selenide.webdriver().driver().hasWebDriverStarted()) {
            System.out.println("✓ Браузер активен, создаем артефакты");
            Attach.screenshot();
            Attach.pageSource();
            
            // Отключаем логи браузера для удаленной среды чтобы избежать WebSocket ошибок
            if (!Configuration.browser.equals("firefox") && !env.equals("remote")) {
                Attach.browserConsoleLogs();
                System.out.println("✓ Собраны логи браузера (локальная среда)");
            } else {
                System.out.println("✓ Логи браузера пропущены (удаленная среда или Firefox)");
            }
            
            if (env.equals("remote")) {
                Attach.addVideo();
                System.out.println("✓ Добавлено видео (удаленная среда)");
            }
            
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            System.out.println("✓ Очищены cookies и localStorage");
        } else {
            System.out.println("⚠ Браузер не активен, пропускаем создание артефактов");
        }
        
        // Принудительно закрываем браузер для удаленной среды чтобы избежать накопления WebSocket соединений
        if (env.equals("remote")) {
            System.out.println("--- Закрытие браузера (удаленная среда) ---");
            try {
                Selenide.closeWebDriver();
                System.out.println("✓ Браузер успешно закрыт");
            } catch (Exception e) {
                // Игнорируем ошибки при закрытии браузера
                System.out.println("⚠ Предупреждение: Не удалось закрыть браузер: " + e.getMessage());
            }
        } else {
            System.out.println("✓ Браузер остается открытым (локальная среда)");
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
        System.out.println("--- Настройка удаленного окружения ---");
        
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        System.out.println("✓ Добавлены Selenoid опции: enableVNC=true, enableVideo=true");

        Configuration.remote = ConfigReader.get("selenoid_url");
        System.out.println("✓ Установлен удаленный URL: " + Configuration.remote);
        
        Configuration.reopenBrowserOnFail = false;
        System.out.println("✓ Отключено автоматическое переоткрытие браузера при ошибках");
    }

    // Базовая конфигурация
    private static void setupBasicConfiguration() {
        System.out.println("--- Базовая конфигурация ---");
        
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 60000;
        System.out.println("✓ Таймауты: timeout=" + Configuration.timeout + "ms, pageLoadTimeout=" + Configuration.pageLoadTimeout + "ms");
        
        Configuration.browser = ConfigReader.get("browser_name");
        Configuration.browserVersion = ConfigReader.get("browser_version");
        Configuration.browserSize = ConfigReader.get("browser_size");
        Configuration.pageLoadStrategy = "eager";
        
        System.out.println("✓ Браузер: " + Configuration.browser + " " + Configuration.browserVersion);
        System.out.println("✓ Размер окна: " + Configuration.browserSize);
        System.out.println("✓ Стратегия загрузки: " + Configuration.pageLoadStrategy);
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
        String env = System.getProperty("env", "local");

        System.out.println("--- Настройка Chrome браузера ---");
        System.out.println("Окружение: " + env);

        // Аргументы командной строки
        chromeOptions.addArguments(
                "--lang=ru",
                "--accept-lang=ru",
                "--disable-blink-features=AutomationControlled");
        System.out.println("✓ Добавлены базовые аргументы Chrome");

        // Дополнительные аргументы для удаленной среды
        if (env.equals("remote")) {
            System.out.println("--- Применение настроек для удаленной среды ---");
            
            chromeOptions.addArguments(
                    "--disable-dev-shm-usage",
                    "--disable-gpu",
                    "--no-sandbox",
                    "--disable-web-security",
                    "--disable-features=VizDisplayCompositor",
                    "--disable-logging",
                    "--silent",
                    "--log-level=3",
                    "--disable-background-timer-throttling",
                    "--disable-backgrounding-occluded-windows",
                    "--disable-renderer-backgrounding"
            );
            System.out.println("✓ Добавлены аргументы для отключения DevTools и логирования");
            
            // Отключаем DevTools и логирование
            chromeOptions.setExperimentalOption("excludeSwitches", java.util.List.of("enable-logging", "enable-automation"));
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            System.out.println("✓ Отключены DevTools и автоматизация");
            
            // Отключаем логирование браузера
            capabilities.setCapability("goog:loggingPrefs", Map.of("browser", "OFF", "driver", "OFF"));
            System.out.println("✓ Отключено логирование браузера и драйвера");
        }

        // Настройки предпочтений
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "ru");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
        System.out.println("✓ Установлены предпочтения браузера");

        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        System.out.println("✓ Chrome опции добавлены в capabilities");
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
