package tests.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import tests.web.constants.CommonData;
import tests.web.constants.NavigationConstants;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byTitle;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class MainPage {

    SelenideElement HEADER_MENU = $(".invitro_header-menu_main-item");
    ElementsCollection CART_ITEMS = $$("li [class*='CartProduct_product__']"),
            BUTTONS = $$("[class*='Button_button']");

    @Step("Открываем главную страницу")
    public MainPage openMainPage() {
        open(CommonData.BASE_URL);
        return this;
    }

    @Step("Открываем адрес: \"{url}\"")
    public MainPage openPage(String url) {
        open(url);
        return this;
    }

    @Step("Выбираем категорию в главном меню: \"{category}\"")
    public MainPage selectMainMenuCategory(String category) {
        HEADER_MENU.$(byText(category))
                .shouldBe(visible, Duration.ofSeconds(10)).click();
        return this;
    }

    @Step("Кликаем на элемент с текстом \"{text}\"")
    public MainPage clickElement(String text) {
        $(byText(text))
                .shouldBe(visible, Duration.ofSeconds(5))
                .click();
        return this;
    }

    @Step("Переходим на страницу номер \"{pageNumber}\"")
    public MainPage goToPage(String pageNumber) {
        SelenideElement paginationElement = $(".catalog_pagination__elem--num[data-num='" + pageNumber + "']")
                .shouldBe(visible, Duration.ofSeconds(10));
        paginationElement.scrollIntoCenter();
        paginationElement.click();
        return this;
    }

    @Step("Находим продукт на странице: \"{productTitle}\"")
    public MainPage findProductOnPage(String productTitle) {
        SelenideElement productCard = $$(".item_card--active").filterBy(text(productTitle)).first();
        productCard.shouldBe(visible, Duration.ofSeconds(10));
        productCard.scrollIntoCenter();
        return this;
    }

    @Step("Добавляем продукт в корзину")
    public MainPage addProductToCart() {
        SelenideElement addToCartButton = $$(byTitle(NavigationConstants.CART_TITLE)).last();
        addToCartButton.shouldBe(visible, Duration.ofSeconds(5));
        addToCartButton.scrollIntoCenter();
        sleep(500);
        addToCartButton.doubleClick();
        return this;
    }

    @Step("Проверяем цену \"{expectedPrice}\" для \"{productTitle}\"")
    public MainPage checkProductInCart(String productTitle, String expectedPrice) {
        ElementsCollection matchingItem = CART_ITEMS
                .filterBy(Condition.text(productTitle))
                .shouldHave(size(1));
        matchingItem.first().$("[class*='CartProduct_productPrice']")
                .shouldHave(Condition.text(expectedPrice));
        return this;
    }

    @Step("Проверяем, что заголовок содержит \"{title}\"")
    public MainPage checkH1(String title) {
        $("h1").shouldHave(Condition.text(title));
        return this;
    }

    @Step("Проверяем, что заголовок содержит \"{title}\"")
    public MainPage checkH2(String title) {
        $("h2").shouldHave(Condition.text(title));
        return this;
    }

    @Step("Проверяем, что кнопка \"{buttonText}\" в состоянии \"{condition}\"")
    public MainPage checkButtonState(String buttonText, String condition) {
        BUTTONS.findBy(Condition.exactText(buttonText))
                .shouldHave(Condition.attribute(condition));
        return this;
    }


}
