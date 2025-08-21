package tests.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

public class DesktopBasePage {

    String CART_TITLE = "В корзину";

    SelenideElement HEADER_MAIN_MENU_TITLE = $(".invitro_header-menu_main-item");
    ElementsCollection CART_ITEMS = $$("li [class*='CartProduct_product__']");

    public DesktopBasePage openPageByURL(String URL) {
        open(URL);
        return this;
    }

    public DesktopBasePage selectHeaderMainMenuTitleDesktop(String category) {
        HEADER_MAIN_MENU_TITLE.$(byText(category))
                .shouldBe(visible, Duration.ofSeconds(10)).click();
        return this;
    }

    public DesktopBasePage selectElement(String text) {
        $(byText(text))
                .shouldBe(visible, Duration.ofSeconds(3))
                .click();
        return this;
    }

    public DesktopBasePage selectPage(String pageNumber) {
        SelenideElement paginationElement = $(".catalog_pagination__elem--num[data-num='" + pageNumber + "']")
                .shouldBe(visible, Duration.ofSeconds(10));
        paginationElement.scrollIntoCenter();
        paginationElement.click();
        return this;
    }

    public DesktopBasePage findProductOnPAge(String productTitle) {
        SelenideElement productCard = $$(".item_card--active").filterBy(text(productTitle)).first();
        productCard.shouldBe(visible, Duration.ofSeconds(10));
        productCard.scrollIntoCenter();
        return this;
    }

    public DesktopBasePage addProductToCart() {
        SelenideElement addToCartButton = $$(byTitle(CART_TITLE)).last();
        addToCartButton.shouldBe(visible, Duration.ofSeconds(5));
        addToCartButton.scrollIntoCenter();
        sleep(500);
        addToCartButton.doubleClick();
        return this;
    }

    public DesktopBasePage checkProductInCart(String productTitle, String expectedPrice) {
        ElementsCollection matchingItem = CART_ITEMS
                .filterBy(Condition.text(productTitle))
                .shouldHave(size(1));
        matchingItem.first().$("[class*='CartProduct_productPrice']")
                .shouldHave(Condition.text(expectedPrice));
        return this;
    }
}
