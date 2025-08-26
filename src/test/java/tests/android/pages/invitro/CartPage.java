package tests.android.pages.invitro;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.appium.AppiumSelectors.byText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class CartPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final SelenideElement CONTAINER = $(id(INVITRO_ID + "container")),
            TOTAL = $(id(INVITRO_ID + "total")),
            TOTAL_PRICE = $(byXpath("//*[@resource-id='com.invitro.app:id/total']" +
                    "/following-sibling::*[@resource-id='com.invitro.app:id/price']"));

    @Step("Проверяем название товара в корзине")
    public CartPage checkProductName(String productName) {
        CONTAINER.$(id("com.invitro.app:id/name"))
                .shouldHave(Condition.text(productName)).scrollTo();
        return this;
    }

    @Step("Проверяем цену товара в корзине")
    public CartPage checkProductPrice(String productPrice) {
        CONTAINER.$(id("com.invitro.app:id/price"))
                .shouldHave(Condition.text(productPrice)).scrollTo();
        return this;
    }

    @Step("Проверяем номер товара в корзине")
    public CartPage checkProductNumber(String productNumber) {
        CONTAINER.$(id("com.invitro.app:id/number"))
                .shouldHave(Condition.text(productNumber)).scrollTo();
        return this;
    }

    @Step("Выполняем скролл до товара в корзине и проверяем название")
    public CartPage scrollAndCheckProductName(String productName) {
        CONTAINER.$(id("com.invitro.app:id/name"))
                .$(byText(productName)).scrollTo();
        return this;
    }

    @Step("Проверяем итоговую цену")
    public CartPage checkTotalPrice(String totalPrice) {
        TOTAL.scrollTo();
        TOTAL_PRICE.shouldHave(Condition.text(totalPrice));
        return this;
    }

}
