package tests.android.pages.invitro;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.SelenideAppiumCollection;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.appium.AppiumSelectors.byText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$$;
import static io.appium.java_client.AppiumBy.id;

public class CartPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final SelenideElement CONTAINER = $(id(INVITRO_ID + "container")),
            TOTAL = $(id(INVITRO_ID + "total")),
            TOTAL_PRICE = $(byXpath("//*[@resource-id='com.invitro.app:id/total']" +
                    "/following-sibling::*[@resource-id='com.invitro.app:id/price']")),
            CHANGE_OFFICE_BUTTON = $(id(INVITRO_ID + "change_office_button")),
            OFFICE_ADDRESS = $(id(INVITRO_ID + "name")),
            OFFICE_OPTIONS = $(id(INVITRO_ID + "tag_name"));

    private final SelenideAppiumCollection OFFICE_ADDRESS_COLLECTION = $$(id(INVITRO_ID + "name"));

    @Step("Проверяем название товара в корзине")
    public CartPage checkProductName(String productName) {
        CONTAINER.$(id("com.invitro.app:id/name"))
                .$(byText(productName))
                .scrollTo();
        return this;
    }

    @Step("Проверяем цену товара в корзине")
    public CartPage checkProductPrice(String productPrice) {
        CONTAINER.$(id("com.invitro.app:id/price"))
                .$(byText(productPrice))
                .scrollTo();
        return this;
    }

    @Step("Проверяем номер товара в корзине")
    public CartPage checkProductNumber(String productNumber) {
        CONTAINER.$(id("com.invitro.app:id/number"))
                .$(byText(productNumber))
                .scrollTo();
        return this;
    }

    @Step("Выполняем скролл до товара в корзине и проверяем название")
    public CartPage scrollAndCheckProductName(String productName) {
        CONTAINER.$(id("com.invitro.app:id/name"))
                .$(byText(productName))
                .scrollTo();
        return this;
    }

    @Step("Проверяем итоговую цену")
    public CartPage checkTotalPrice(String totalPrice) {
        TOTAL.shouldBe(Condition.visible, Duration.ofSeconds(5)).scrollTo();
        TOTAL_PRICE.shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text(totalPrice));
        return this;
    }

    @Step("Нажимаем на кнопку смены офиса")
    public CartPage changeOfficeButton() {
        CHANGE_OFFICE_BUTTON.click();
        return this;
    }

    @Step("Выбираем офис в списке")
    public CartPage choseOfficeByList(String address) {
        OFFICE_ADDRESS.$(byText(address))
                .scrollTo()
                .click();
        return this;
    }

    @Step("Проверяем, что свойства офиса отображаются на странице")
    public CartPage checkOfficeProperty() {
        OFFICE_OPTIONS.is(Condition.visible, Duration.ofMillis(1500));
        return this;
    }

    @Step("Проверяем адрес выбранного офиса в корзине")
    public CartPage checkAddress(String address) {
        OFFICE_ADDRESS_COLLECTION
                .findBy(Condition.text(address))
                .is(Condition.visible);
        return this;
    }

}
