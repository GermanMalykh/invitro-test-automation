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

public class MainPage {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int SHORT_TIMEOUT_SECONDS = 5;
    private static final String PAGINATION_SELECTOR = ".catalog_pagination__elem--num[data-num='%s']";
    private static final String PRODUCT_PRICE_SELECTOR = "[class*='CartProduct_productPrice']";
    private static final String ACTIVE_PRODUCT_CARD_SELECTOR = ".item_card--active";
    private static final String HEADER_MENU_SELECTOR = ".invitro_header-menu_main-item";
    private static final String CART_ITEMS_SELECTOR = "li [class*='CartProduct_product__']";
    private static final String BUTTONS_SELECTOR = "[class*='Button_button']";

    SelenideElement HEADER_MENU = $(HEADER_MENU_SELECTOR);
    ElementsCollection CART_ITEMS = $$(CART_ITEMS_SELECTOR),
            BUTTONS = $$(BUTTONS_SELECTOR);

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
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS)).click();
        return this;
    }

    @Step("Кликаем на элемент с текстом \"{text}\"")
    public MainPage clickElement(String text) {
        $(byText(text))
                .scrollTo()
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                .click();
        return this;
    }

    @Step("Переходим на страницу номер \"{pageNumber}\"")
    public MainPage goToPage(String pageNumber) {
        SelenideElement paginationElement = $(String.format(PAGINATION_SELECTOR, pageNumber))
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        paginationElement.scrollIntoCenter();
        paginationElement.click();
        return this;
    }

    @Step("Находим продукт на странице: \"{productTitle}\"")
    public MainPage findProductOnPage(String productTitle) {
        SelenideElement productCard = $$(ACTIVE_PRODUCT_CARD_SELECTOR).filterBy(text(productTitle)).first();
        productCard.shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        productCard.scrollIntoCenter();
        return this;
    }

    @Step("Добавляем продукт в корзину")
    public MainPage addProductToCart() {
        SelenideElement addToCartButton = $$(byTitle(NavigationConstants.CART_TITLE)).last();
        addToCartButton.shouldBe(visible, Duration.ofSeconds(SHORT_TIMEOUT_SECONDS));
        addToCartButton.scrollIntoCenter();
        addToCartButton.doubleClick();
        return this;
    }

    @Step("Проверяем цену \"{expectedPrice}\" для \"{productTitle}\"")
    public MainPage checkProductInCart(String productTitle, String expectedPrice) {
        ElementsCollection matchingItem = CART_ITEMS
                .filterBy(Condition.text(productTitle))
                .shouldHave(size(1));
        matchingItem.first().$(PRODUCT_PRICE_SELECTOR)
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
