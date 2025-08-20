package tests.web.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byTitle;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class DesktopBasePage {

    String CART_TITLE = "В корзину";

    SelenideElement HEADER_MAIN_MENU_TITLE = $(".invitro_header-menu_main-item"),
            CART_PRODUCTS_NAME = $("[class*='CartProductsPanel_cartProducts']");

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

    public SelenideElement selectElementByParent(String text) {
        return $(byText(text))
                .shouldBe(visible, Duration.ofSeconds(3))
                .parent();
    }

    public DesktopBasePage clickByElement(SelenideElement element) {
        executeJavaScript(
                "const el=arguments[0];" +
                        "const cs=getComputedStyle(el,'::before');" +
                        "const r =el.getBoundingClientRect();" +
                        "const right=parseFloat(cs.right)||20;" +
                        "const bl   =parseFloat(cs.borderLeftWidth)||6;" +
                        "const x = Math.round(r.left + r.width  - right - bl/2);" +
                        "const y = Math.round(r.top  + r.height / 2);" +
                        "const tgt=document.elementFromPoint(x,y) || el;" +
                        "['pointermove','mousemove','mouseenter','mouseover'].forEach(t=>" +
                        "  tgt.dispatchEvent(new MouseEvent(t,{clientX:x,clientY:y,bubbles:true,cancelable:true,view:window}))" +
                        ");" +
                        "tgt.dispatchEvent(new MouseEvent('click',{clientX:x,clientY:y,bubbles:true,cancelable:true,view:window}));",
                element
        );
        return this;
    }

    public DesktopBasePage selectPage(String pageNumber) {
        SelenideElement paginationElement = $(".catalog_pagination__elem--num[data-num='" + pageNumber + "']")
                .shouldBe(visible, Duration.ofSeconds(10));
        executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", paginationElement);
        paginationElement.click();
        return this;
    }

    public DesktopBasePage findProductOnPAge(String productTitle) {
        SelenideElement productCard = $$(".item_card--active").filterBy(text(productTitle)).first();
        productCard.shouldBe(visible, Duration.ofSeconds(10));
        executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", productCard);
        return this;
    }

    public DesktopBasePage addProductToCart() {
        SelenideElement addToCartButton = $$(byTitle(CART_TITLE)).last();
        addToCartButton.shouldBe(visible, Duration.ofSeconds(5));
        executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", addToCartButton);
        sleep(500);
        addToCartButton.doubleClick();
        return this;
    }

    public DesktopBasePage checkProductNameInCart(String productTitle) {
        CART_PRODUCTS_NAME.shouldHave(text(productTitle));
        return this;
    }
}
