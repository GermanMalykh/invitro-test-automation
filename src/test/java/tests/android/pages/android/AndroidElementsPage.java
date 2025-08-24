package tests.android.pages.android;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.appium.SelenideAppium.$;

import static io.appium.java_client.AppiumBy.id;

public class AndroidElementsPage {

    private final SelenideElement SEARCH_BAR_ELEMENT = $(id("android:id/search_src_text")),
            PERMISSION_DENY_BUTTON = $(id("com.android.permissioncontroller:id/permission_deny_button"));

    @Step("Установка текста в поисковую строку")
    public AndroidElementsPage setSearchText(String text) {
        SEARCH_BAR_ELEMENT.type(text);
        return this;
    }

    @Step("Отклонение разрешения на геолокацию")
    public AndroidElementsPage locationPermissionDeny() {
        PERMISSION_DENY_BUTTON.click();
        return this;
    }

}
