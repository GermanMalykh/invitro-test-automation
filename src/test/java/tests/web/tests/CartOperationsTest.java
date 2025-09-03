package tests.web.tests;

import constants.UrlConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.web.constants.CartConstants;
import tests.web.base.PageManager;

import static io.qameta.allure.Allure.step;
import static constants.CommonConstants.ANALYSIS_CATEGORY;
import static constants.CommonConstants.ANALYSIS_COMPLEXES_CATEGORY;
import static constants.CommonConstants.COVID_19_CATEGORY;
import static constants.CommonConstants.PAGE_NUMBER_2;
import static tests.web.constants.ProductConstants.PRODUCT_TITLE;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("Web Tests")
public class CartOperationsTest extends PageManager {

    String productPrice;

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Добавление продукта в корзину и проверка информации")
    @Description("Тест проверяет полный процесс добавления продукта в корзину через главное меню и корректное отображения информации")
    void addProductToCartAndCheckInfo() {
        step("Открываем главную страницу", () -> {
            main.openMainPage();
        });
        step("Переходим в каталог продукции", () -> {
            main.selectMainMenuCategory(ANALYSIS_CATEGORY);
            base.clickByText(ANALYSIS_COMPLEXES_CATEGORY)
                    .clickByText(COVID_19_CATEGORY);
        });
        step("Закрываем cookie-баннер", () -> {
            cookie.closeCookieBanner();
        });
        step("Выполняем навигацию по страницам каталога", () -> {
            catalog.goToPage(PAGE_NUMBER_2);
        });
        step("Сохраняем цену продукта", () -> {
            productPrice = catalog.extractProductPriceFromPage();
        });
        step("Добавляем продукт в корзину", () -> {
            catalog.findProductOnPage(PRODUCT_TITLE)
                    .addProductToCart();
        });
        step("Проверяем цену продукта в корзине", () -> {
            cart.checkProductPrice(PRODUCT_TITLE, productPrice);
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Очистка корзины и проверка информации")
    @Description("Тест проверяет функциональность очистки корзины и корректное отображение пустого состояния")
    void cartCleanAndCheckEmptyInfo() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts();
        });
        step("Переходим в корзину", () -> {
            base.openPage(UrlConstants.CART_URL);
        });
        step("Очищаем корзину и проверяем её состояние", () -> {
            base.clickByText(CartConstants.CLEAN_CERT)
                    .checkH1(CartConstants.EMPTY_CART);
        });
    }

}
