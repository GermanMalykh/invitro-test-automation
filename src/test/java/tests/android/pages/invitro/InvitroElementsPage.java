package tests.android.pages.invitro;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.AppiumSelectors.withText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class InvitroElementsPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final ElementsCollection CITY_NAME = $$(id(INVITRO_ID + "name")),
            CITY_MENU = $$(id(INVITRO_ID + "title_text"));

    private final SelenideElement LOADER_ELEMENT = $(id(INVITRO_ID + "loader")),
            TOOLBAR_CLOSE_BUTTON = $(id(INVITRO_ID + "toolbar"))
                    .$(id(INVITRO_ID + "backImageView")),
            CITY_LIST = $(id(INVITRO_ID + "city_list")),
            AUTH_EXIT_SCREEN = $(id(INVITRO_ID + "rightActionImageView")),
            INZ_INPUT = $(id(INVITRO_ID + "inzText")),
            BIRTH_DATE_INPUT = $(id(INVITRO_ID + "birthdayText")),
            SURNAME_INPUT = $(id(INVITRO_ID + "surnameText")),
            ACCEPT_CHECK_RESULTS = $(id(INVITRO_ID + "create_new_appointment")),
            ERROR_TEXT = $(id(INVITRO_ID + "textinput_error")),
            TIMER_ALERT_MESSAGE = $(id(INVITRO_ID + "alertMessageContainer")),
            BASKET = $(id(INVITRO_ID + "basket")),
            CITIES_LIST = $(id(INVITRO_ID + "letter"));

    @Step("Ожидание исчезновения лоадера")
    public InvitroElementsPage waitForLoaderToDisappear() {
        LOADER_ELEMENT.should(disappear, Duration.ofSeconds(15));
        return this;
    }

    @Step("Закрытие тулбара")
    public InvitroElementsPage closeToolbar() {
        TOOLBAR_CLOSE_BUTTON.should(appear, Duration.ofSeconds(15)).click();
        return this;
    }

    @Step("Проверка плейсхолдера города")
    public InvitroElementsPage verifyCityPlaceholder(String placeholderText) {
        CITY_LIST.$(withText(placeholderText))
                .should(appear);
        return this;
    }

    @Step("Выбор города")
    public InvitroElementsPage selectCity(String city) {
        CITIES_LIST.shouldBe(visible, Duration.ofSeconds(15));
        CITY_NAME.findBy(Condition.text(city))
                .scrollTo()
                .shouldBe(visible, Duration.ofSeconds(15))
                .click();
        return this;
    }

    @Step("Выбор пункта меню города")
    public InvitroElementsPage selectCityMenuItem(String menuName) {
        CITY_MENU.findBy(Condition.text(menuName))
                .shouldBe(visible, Duration.ofSeconds(15)).click();
        return this;
    }

    @Step("Закрытие экрана авторизации")
    public InvitroElementsPage closeAuthScreen() {
        AUTH_EXIT_SCREEN.click();
        return this;
    }

    @Step("Установка ИНЗ")
    public InvitroElementsPage setInz(String inz) {
        INZ_INPUT.setValue(inz);
        return this;
    }

    @Step("Установка даты рождения")
    public InvitroElementsPage setBirthDate(String birthDate) {
        BIRTH_DATE_INPUT.setValue(birthDate);
        return this;
    }

    @Step("Установка фамилии")
    public InvitroElementsPage setSurname(String surname) {
        SURNAME_INPUT.setValue(surname);
        return this;
    }

    @Step("Подтверждение проверки результатов")
    public InvitroElementsPage acceptCheckResult() {
        ACCEPT_CHECK_RESULTS.click();
        return this;
    }

    @Step("Проверка текста ошибки")
    public InvitroElementsPage checkErrorText(String expectedError) {
        ERROR_TEXT.shouldHave(Condition.text(expectedError));
        return this;
    }

    @Step("Проверка сообщения о таймере ожидания")
    public InvitroElementsPage checkCooldownTimerMessage() {
        TIMER_ALERT_MESSAGE
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldBe(Condition.attribute("displayed", "true"));
        return this;
    }

    @Step("Переходим в корзину")
    public InvitroElementsPage navigateToBasket() {
        BASKET.click();
        return this;
    }

    @Step("Проверяем отображение секции \"{section}\" в меню")
    public InvitroElementsPage verifySectionDisplayed(String section) {
        CITY_MENU.findBy(Condition.attribute("text", section));
        return this;
    }

    /**
     * Валидатор текста ошибок при различных попытках проверки анализов
     *
     * @param attempts
     * @return
     */
    public String getRemainingAttemptsText(int attempts) {
        if (attempts == 1) {
            return "Осталась 1 попытка";
        }
        if (attempts >= 2 && attempts <= 4) {
            return "Осталось " + attempts + " попытки";
        }
        return "Осталось " + attempts + " попыток";
    }

}
