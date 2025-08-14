package tests.android.tests;

import org.junit.jupiter.api.Test;
import tests.android.config.PreRunConfig;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class BasicTests extends PreRunConfig {

    @Test
    void basicTest(){
        $(id("com.invitro.app:id/navigation_bar_item_large_label_view")).click();
    }
}
