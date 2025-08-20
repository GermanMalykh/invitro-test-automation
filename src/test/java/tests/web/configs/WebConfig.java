package tests.web.configs;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class WebConfig {

    @BeforeAll
    static void setup(){
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }
}
