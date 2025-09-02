package tests.android.pages.invitro;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.AppiumSelectors.byText;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class MedicalTestElementsPage {

    public final String INVITRO_ID = "com.invitro.app:id/";

    private final SelenideAppiumElement TAB_LIST = $(id(INVITRO_ID + "tabLayout")),
            ITEM_LIST = $(id(INVITRO_ID + "itemList")),
            ITEMS_TITLE = $(id(INVITRO_ID + "text_title")),
            NAME = $(id(INVITRO_ID + "name")),
            CART_BUTTON = $(id(INVITRO_ID + "cart_button")),
            PROGRESS = $(id(INVITRO_ID + "progress"));

    @Step("Выбор категории анализов: {tabName}")
    public MedicalTestElementsPage selectCategory(String tabName) {
        PROGRESS.shouldBe(Condition.hidden,Duration.ofSeconds(20));
        ITEM_LIST.shouldBe(visible);
        TAB_LIST.$(byText(tabName))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Выбор анализа из категории: {itemName}")
    public MedicalTestElementsPage selectItem(String itemName) {
        PROGRESS.shouldBe(Condition.hidden,Duration.ofSeconds(20));
        ITEM_LIST.shouldBe(visible);
        $(byText(itemName))
                .scrollTo()
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Проверяем заголовок для выбранного анализа")
    public MedicalTestElementsPage checkItemsTitle(String itemsTitle) {
        ITEMS_TITLE
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text(itemsTitle));
        return this;
    }

    @Step("Выбираем анализ из списка: {testName}")
    public MedicalTestElementsPage selectTest(String testName) {
        $(byText(testName))
                .scrollTo()
                .click();
        return this;
    }

    @Step("Проверяем имя для выбранного анализа: {itemsName}")
    public MedicalTestElementsPage checkItemsName(String itemsName) {
        NAME.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text(itemsName));
        return this;
    }

    @Step("Добавляем товар в корзину")
    public MedicalTestElementsPage addItemToCart() {
        CART_BUTTON
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.attribute("text", "Добавить в корзину"))
                .click();
        return this;
    }

    @Step("Проверяем состояние кнопки добавления товара в корзину")
    public MedicalTestElementsPage checkButtonCart() {
        CART_BUTTON
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.attribute("text", "В корзине"));
        return this;
    }

}
