package tests.android.pages.invitro;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.appium.SelenideAppiumCollection;
import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.AppiumSelectors.byText;
import static com.codeborne.selenide.appium.AppiumSelectors.withText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$$;
import static io.appium.java_client.AppiumBy.id;

public class InvitroElementsPage {

    public final String INVITRO_ID = "com.invitro.app:id/",
            TOOLBAR = INVITRO_ID + "backImageView";

    private final SelenideAppiumCollection CITY_MENU = $$(id(INVITRO_ID + "title_text"));

    private final SelenideAppiumElement LOADER_ELEMENT = $(id(INVITRO_ID + "loader")),
            TOOLBAR_CLOSE_BUTTON = $(id(INVITRO_ID + "toolbar")),
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

    @Step("Ожидаем исчезновения лоадера")
    public InvitroElementsPage waitForLoaderToDisappear() {
        LOADER_ELEMENT.should(disappear, Duration.ofSeconds(15));
        return this;
    }

    @Step("Закрываем тулбар")
    public InvitroElementsPage closeToolbar() {
        TOOLBAR_CLOSE_BUTTON
                .$(id(TOOLBAR))
                .should(appear, Duration.ofSeconds(15))
                .click();
        return this;
    }

    @Step("Проверяем плейсхолдер \"{placeholderText}\" для города")
    public InvitroElementsPage checkCityPlaceholder(String placeholderText) {
        CITY_LIST
                .$(withText(placeholderText))
                .should(appear);
        return this;
    }

    @Step("Выбираем город \"{city}\"")
    public InvitroElementsPage selectCity(String city) {
        CITIES_LIST
                .shouldBe(visible, Duration.ofSeconds(15));
        $(byText(city))
                .scrollTo()
                .shouldBe(visible, Duration.ofSeconds(15))
                .click();
        return this;
    }

    @Step("Выбираем пункт \"{menuName}\" в меню города")
    public InvitroElementsPage selectCityMenuItem(String menuName) {
        CITY_MENU
                .findBy(Condition.text(menuName))
                .shouldBe(visible, Duration.ofSeconds(15)).click();
        return this;
    }

    @Step("Закрываем экран авторизации")
    public InvitroElementsPage closeAuthScreen() {
        AUTH_EXIT_SCREEN.click();
        return this;
    }

    @Step("Устанавливаем ИНЗ: \"{inz}\"")
    public InvitroElementsPage setInz(String inz) {
        INZ_INPUT.setValue(inz);
        return this;
    }

    @Step("Устанавливаем дату рождения: \"{birthDate}\"")
    public InvitroElementsPage setBirthDate(String birthDate) {
        BIRTH_DATE_INPUT.setValue(birthDate);
        return this;
    }

    @Step("Устанавливаем фамилию: \"{surname}\"")
    public InvitroElementsPage setSurname(String surname) {
        SURNAME_INPUT.setValue(surname);
        return this;
    }

    @Step("Подтверждаем проверку результатов")
    public InvitroElementsPage acceptCheckResult() {
        ACCEPT_CHECK_RESULTS.click();
        return this;
    }

    @Step("Проверяем текст ошибки: \"{expectedError}\"")
    public InvitroElementsPage checkErrorText(String expectedError) {
        ERROR_TEXT.shouldHave(Condition.text(expectedError));
        return this;
    }

    @Step("Проверяем сообщение о таймере ожидания")
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
    public InvitroElementsPage checkSectionDisplayed(String section) {
        CITY_MENU.findBy(Condition.attribute("text", section));
        return this;
    }

    @Step("Проверяем текст ошибки")
    public InvitroElementsPage checkErrorTextForAttempt(int attempts) {
        String expectedEnding = getRemainingAttemptsText(attempts);
        String expectedError = String.format("По введенным данным результатов не найдено. %s", expectedEnding);
        ERROR_TEXT.shouldHave(Condition.text(expectedError));
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
