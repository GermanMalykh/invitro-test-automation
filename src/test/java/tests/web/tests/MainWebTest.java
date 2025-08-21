package tests.web.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import tests.web.configs.WebConfig;
import tests.web.pages.BannersPage;
import tests.web.pages.CookiePage;
import tests.web.pages.DesktopMainPage;

import static tests.web.constants.TestData.MENU_CATEGORY;
import static tests.web.constants.TestData.PAGE_NUMBER_2;
import static tests.web.constants.TestData.PRODUCT_CATEGORY;
import static tests.web.constants.TestData.PRODUCT_TITLE;
import static tests.web.constants.TestData.SIDEBAR_MENU_CATEGORY;

public class MainWebTest extends WebConfig {

    DesktopMainPage desktop = new DesktopMainPage();
    BannersPage banner = new BannersPage();

    @Test
    void testAddProductInCartAndCheckInfo() {
        desktop.openMainPage()
                .selectHeaderMainMenuTitleDesktop(MENU_CATEGORY)
                .selectElement(SIDEBAR_MENU_CATEGORY)
                .selectElement(PRODUCT_CATEGORY);
        banner.closeCookie();
        desktop.selectPage(PAGE_NUMBER_2);
        String productPrice = CookiePage.CART_ITEMS
                .filterBy(Condition.text(PRODUCT_TITLE))
                .first()
                .$(".analyzes-item__total--sum")
                .getText();
        desktop.findProductOnPAge(PRODUCT_TITLE);
        desktop.addProductToCart()
                .checkProductInCart(PRODUCT_TITLE, productPrice);
    }
}
