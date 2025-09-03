package tests.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    private static final String PRODUCT_PRICE_SELECTOR = "[class*='CartProduct_productPrice']";

    private static final ElementsCollection CART_ITEMS = $$("li [class*='CartProduct_product__']");

    @Step("Проверяем цену \"{expectedPrice}\" для \"{productTitle}\"")
    public CartPage checkProductPrice(String productTitle, String expectedPrice) {
        CART_ITEMS
                .filterBy(Condition.text(productTitle))
                .shouldHave(size(1))
                .first().$(PRODUCT_PRICE_SELECTOR)
                .shouldHave(Condition.text(expectedPrice));
        return this;
    }

}
