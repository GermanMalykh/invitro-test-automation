package tests.android.pages.android;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;

import static io.appium.java_client.AppiumBy.id;

public class AndroidElementsPage {

    private final SelenideElement SEARCH_BAR_ELEMENT = $(id("android:id/search_src_text")),
            PERMISSION_DENY_BUTTON = $(id("com.android.permissioncontroller:id/permission_deny_button"));

    @Step("Поиск города: {city}")
    public AndroidElementsPage setSearchText(String city) {
        SEARCH_BAR_ELEMENT.type(city);
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
