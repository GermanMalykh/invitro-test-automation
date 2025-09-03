package tests.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class BasePage {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private static final ElementsCollection BUTTONS = $$("[class*='Button_button']");

    @Step("Открываем адрес: \"{url}\"")
    public BasePage openPage(String url) {
        open(url);
        return this;
    }

    @Step("Кликаем на элемент с текстом \"{text}\"")
    public BasePage clickByText(String text) {
        $(byText(text))
                .scrollTo()
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                .click();
        return this;
    }

    @Step("Проверяем наличие заголовка \"{title}\"")
    public BasePage checkH1(String title) {
        $("h1").shouldHave(Condition.text(title));
        return this;
    }

    @Step("Проверяем наличие заголовка \"{title}\"")
    public BasePage checkH2(String title) {
        $("h2").shouldHave(Condition.text(title));
        return this;
    }

    @Step("Проверяем, что кнопка \"{buttonText}\" находится в состоянии \"{condition}\"")
    public BasePage checkButtonState(String buttonText, String condition) {
        BUTTONS.findBy(Condition.exactText(buttonText))
                .shouldHave(Condition.attribute(condition));
        return this;
    }

}
