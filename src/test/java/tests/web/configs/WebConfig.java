package tests.web.configs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class WebConfig {

    @BeforeAll
    static void setup() {
        // Проверяем, что это действительно Web тест
        String testType = System.getProperty("test.type");
        
        // Web тесты могут запускаться в любом режиме, так как мы принудительно устанавливаем Chrome
        Assumptions.assumeTrue("web".equals(testType) || "all".equals(testType) || testType == null, 
            "Этот тест должен запускаться только для Web тестов или всех тестов");
        
        // ПРИНУДИТЕЛЬНО устанавливаем Chrome браузер для Web тестов
        Configuration.browser = "chrome";
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }

    @AfterEach
    void clearCartFromLocalStorage() {
        executeJavaScript("localStorage.removeItem('flockapi:2752:cart');");
        WebDriverRunner.getWebDriver().manage().deleteCookieNamed("INVITRO_CART");
    }
}
