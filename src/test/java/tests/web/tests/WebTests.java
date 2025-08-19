package tests.web.tests;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class WebTests {

    @Test
    void webTest() {
        open("https://invitro.ru/");
        sleep(60_0000);
    }
}
