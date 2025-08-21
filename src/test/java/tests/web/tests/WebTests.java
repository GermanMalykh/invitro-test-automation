package tests.web.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import tests.web.configs.WebConfig;
import tests.web.pages.BannersPage;
import tests.web.pages.CartPage;
import tests.web.pages.DesktopBasePage;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class WebTests extends WebConfig {

    DesktopBasePage dp = new DesktopBasePage();
    BannersPage bp = new BannersPage();
    CartPage cp = new CartPage();

    String MENU_CATEGORY = "Анализы",
            PRODUCT_CATEGORY = "COVID-19",
            BASE_URL = "https://invitro.ru/",
            PRODUCT_TITLE = "Контрольное исследование после перенесенной острой респираторной вирусной инфекции",
            SIDEBAR_MENU_CATEGORY = "Комплексы анализов",
            PAGE_NUMBER_2 = "2";

    @Test
    void testAddAndCheckProductInfoInCart() {
        dp.openPageByURL(BASE_URL);
        bp.closePromoBanner();
        dp.selectHeaderMainMenuTitleDesktop(MENU_CATEGORY)
                .selectElement(SIDEBAR_MENU_CATEGORY)
                .selectElement(PRODUCT_CATEGORY);
        bp.closeCookieBanner().closePromoBanner();
        dp.selectPage(PAGE_NUMBER_2);
        String productPrice = CartPage.CART_ITEMS
                .filterBy(Condition.text(PRODUCT_TITLE))
                .first()
                .$(".analyzes-item__total--sum")
                .getText();
        bp.closePromoBanner();
        dp.findProductOnPAge(PRODUCT_TITLE);
        bp.closePromoBanner();
        dp.addProductToCart()
                .checkProductInCart(PRODUCT_TITLE, productPrice);
    }

    @Test
    void testCartClean() throws IOException {
        cp.addingProductsCookie();
        open("https://lk3.invitro.ru/cart");
        $(byText("Очистить корзину")).click();
        $("h1").shouldHave(Condition.text("Ваша корзина пуста"));
    }

    @Test
    void testChoosingOfficeInCart() throws IOException {
        cp.addingProductsCookie();
        open("https://lk3.invitro.ru/cart");
        $(byText("В ИНВИТРО")).click();
        $(byText("Выбрать медицинский офис")).click();
        $(byText("Севастополь, ул. Адмирала Октябрьского, д. 8")).shouldBe(visible).click();
        $(byText("Выбрать офис")).click();
        $(byText("Оформить заказ")).click();
        $("h1").shouldHave(Condition.text("Оформление заказа"));
    }

}
