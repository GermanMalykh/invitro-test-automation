package tests.android.pages.invitro;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.appium.AppiumSelectors.withText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class InvitroElementsPage {

    public final static String INVITRO_ID = "com.invitro.app:id/";
    private final static SelenideElement LOADER_ELEMENT = $(id(INVITRO_ID + "loader")),
            TOOLBAR_CLOSE_BUTTON = $(id(INVITRO_ID + "toolbar"))
                    .$(id(INVITRO_ID + "backImageView")),
            CITY_LIST = $(id(INVITRO_ID + "city_list"));


    public InvitroElementsPage waitForLoaderToDisappear() {
        LOADER_ELEMENT.should(disappear, Duration.ofSeconds(15));
        return this;
    }

    public InvitroElementsPage closeToolbar() {
        TOOLBAR_CLOSE_BUTTON.should(appear).click();
        return this;
    }

    public InvitroElementsPage verifyCityPlaceholder(String placeholderText) {
        CITY_LIST.$(withText(placeholderText))
                .should(appear);
        return this;
    }

}
