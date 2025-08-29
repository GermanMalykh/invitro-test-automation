package tests.web.tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.web.config.WebConfig;
import tests.web.pages.CookieBannerPage;
import tests.web.pages.CookieManagerPage;
import tests.web.pages.MainPage;

import static io.qameta.allure.Allure.step;
import static tests.web.constants.NavigationConstants.MENU_CATEGORY;
import static tests.web.constants.NavigationConstants.PAGE_NUMBER_2;
import static tests.web.constants.NavigationConstants.PRODUCT_CATEGORY;
import static tests.web.constants.NavigationConstants.SIDEBAR_MENU_CATEGORY;
import static tests.web.constants.ProductConstants.PRODUCT_TITLE;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("[Web] Main Flow Tests")
public class MainFlowWebTests extends WebConfig {

    MainPage desktop = new MainPage();
    CookieBannerPage banner = new CookieBannerPage();

    private String productPrice;

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Добавление продукта в корзину и проверка информации")
    @Description("Тест проверяет полный процесс добавления продукта в корзину через главное меню и корректность отображения информации")
    void testAddProductInCartAndCheckInfo() {
        step("Открываем главную страницу и переходим в категорию меню", () -> {
            desktop.openMainPage()
                    .selectMainMenuCategory(MENU_CATEGORY)
                    .clickElement(SIDEBAR_MENU_CATEGORY)
                    .clickElement(PRODUCT_CATEGORY);
        });
        step("Закрываем cookie-баннер", () -> {
            banner.closeCookieBanner();
        });
        step("Переходим на вторую страницу каталога", () -> {
            desktop.goToPage(PAGE_NUMBER_2);
        });
        step("Получаем цену продукта из корзины", () -> {
            productPrice = CookieManagerPage.CART_ITEMS
                    .filterBy(Condition.text(PRODUCT_TITLE))
                    .first()
                    .$(".analyzes-item__total--sum")
                    .getText();
        });
        step("Находим продукт на странице и добавляем его в корзину", () -> {
            desktop.findProductOnPage(PRODUCT_TITLE);
            desktop.addProductToCart()
                    .checkProductInCart(PRODUCT_TITLE, productPrice);
        });
    }
}
