package tests.web.configs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class WebConfig {

    @BeforeAll
    static void setup() {
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
