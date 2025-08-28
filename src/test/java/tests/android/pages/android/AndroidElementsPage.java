package tests.android.pages.android;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.commands.AppiumClick;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$$;
import static com.codeborne.selenide.appium.AppiumSelectors.withText;

import static io.appium.java_client.AppiumBy.id;
import static io.appium.java_client.AppiumBy.xpath;

public class AndroidElementsPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final SelenideElement SEARCH_BAR_ELEMENT = $(id("android:id/search_src_text")),
            PERMISSION_DENY_BUTTON = $(id("com.android.permissioncontroller:id/permission_deny_button")),
            CITIES_LIST = $(id(INVITRO_ID + "letter"));

    @Step("Поиск города: {city}")
    public AndroidElementsPage setSearchText(String city) {
        CITIES_LIST.shouldBe(visible, Duration.ofSeconds(15));
        SEARCH_BAR_ELEMENT
                .shouldBe(Condition.visible,Duration.ofSeconds(2))
                .type(city);
        return this;
    }

    @Step("Отклонение разрешения на геолокацию")
    public AndroidElementsPage locationPermissionDeny() {
        if ($(id("com.android.permissioncontroller:id/grant_dialog"))
                .is(Condition.visible, Duration.ofMillis(1500))) {
            PERMISSION_DENY_BUTTON.click();
        }
        return this;
    }

}
