package tests.web.pages;

import com.codeborne.selenide.SelenideElement;
import constants.UrlConstants;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private static final SelenideElement HEADER_MENU = $(".invitro_header-menu_main-item");

    @Step(UrlConstants.INVITRO_MAIN)
    public MainPage openMainPage() {
        open(UrlConstants.INVITRO_MAIN);
        return this;
    }

    @Step("Выбираем категорию: \"{category}\"")
    public MainPage selectMainMenuCategory(String category) {
        HEADER_MENU.$(byText(category))
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS)).click();
        return this;
    }

}
