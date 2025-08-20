package tests.android.pages.android;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.appium.SelenideAppium.$;

import static io.appium.java_client.AppiumBy.id;

public class AndroidElementsPage {
    private final String ANDROID_ID = "android:id/";
    private final SelenideElement SEARCH_BAR_ELEMENT = $(id(ANDROID_ID + "search_src_text"));

    public AndroidElementsPage setSearchingText(String text) {
        SEARCH_BAR_ELEMENT.setValue(text);
        return this;
    }
}
