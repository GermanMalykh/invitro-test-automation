package tests.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import constants.CommonConstants;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTitle;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static tests.web.constants.ProductConstants.PRODUCT_TITLE;

public class CatalogPage {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int SHORT_TIMEOUT_SECONDS = 5;

    private static final String PAGINATION_SELECTOR = ".catalog_pagination__elem--num[data-num='%s']";
    private static final String ACTIVE_PRODUCT_CARD_SELECTOR = ".item_card--active";
    private static final String PRODUCT_PRICE = ".analyzes-item__total--sum";

    private static final ElementsCollection ITEM_PRICE = $$(".analyzes-item");

    @Step("Переходим на страницу номер \"{pageNumber}\"")
    public CatalogPage goToPage(String pageNumber) {
        SelenideElement paginationElement = $(String.format(PAGINATION_SELECTOR, pageNumber))
                .shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        paginationElement.scrollIntoCenter();
        paginationElement.click();
        return this;
    }

    @Step("Находим на странице: \"{productTitle}\"")
    public CatalogPage findProductOnPage(String productTitle) {
        SelenideElement productCard = $$(ACTIVE_PRODUCT_CARD_SELECTOR).filterBy(text(productTitle)).first();
        productCard.shouldBe(visible, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        productCard.scrollIntoCenter();
        return this;
    }

    @Step("Добавляем продукт в корзину")
    public CatalogPage addProductToCart() {
        SelenideElement addToCartButton = $$(byTitle(CommonConstants.TO_CART)).last();
        addToCartButton.shouldBe(visible, Duration.ofSeconds(SHORT_TIMEOUT_SECONDS));
        addToCartButton.scrollIntoCenter();
        addToCartButton.doubleClick();
        return this;
    }

    @Step("Извлекаем стоимость товара со страницы")
    public String extractProductPriceFromPage() {
        return ITEM_PRICE
                .filterBy(Condition.text(PRODUCT_TITLE))
                .first()
                .$(PRODUCT_PRICE)
                .getText();
    }

}
