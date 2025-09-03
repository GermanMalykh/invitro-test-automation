package tests.web.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import tests.web.constants.CommonData;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private static final SelenideElement HEADER_MENU = $(".invitro_header-menu_main-item");

    @Step(CommonData.BASE_URL)
    public MainPage openMainPage() {
        open(CommonData.BASE_URL);
        return this;
    }

    @Step("Выбираем категорию: \"{category}\"")
    public MainPage selectMainMenuCategory(String category) {
        HEADER_MENU.$(byText(category))
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS)).click();
        return this;
    }

}
