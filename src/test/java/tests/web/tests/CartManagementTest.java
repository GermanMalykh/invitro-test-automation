package tests.web.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.web.configs.WebConfig;
import tests.web.constants.CartConstants;
import tests.web.constants.ProductConstants;
import tests.web.constants.CommonData;
import tests.web.models.CitiesInfo;
import tests.web.models.ProductsInfo;
import tests.web.pages.CookieManagerPage;
import tests.web.pages.MainPage;

import static io.qameta.allure.Allure.step;
import static tests.web.constants.CommonData.CART_URL;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("[Web] Cart Management Tests")
public class CartManagementTest extends WebConfig {

    MainPage desktop = new MainPage();
    CookieManagerPage cookie = new CookieManagerPage();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Очистка корзины и проверка её состояния")
    @Description("Тест проверяет функциональность очистки корзины и корректное отображение пустого состояния")
    void testCartClean() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts();
        });
        step("Переходим в корзину", () -> {
            desktop.openPage(CommonData.CART_URL);
        });
        step("Очищаем корзину и проверяем её состояние", () -> {
            desktop.clickElement(CartConstants.CLEAN_CERT);
            desktop.checkH1(CartConstants.EMPTY_CART);
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Выбор офиса для сдачи анализов в корзине")
    @Description("Тест проверяет процесс выбора медицинского офиса для сдачи анализов и переход к оформлению заказа")
    void testChoosingOfficeInCart() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts();
        });
        step("Переходим в корзину", () -> {
            desktop.openPage(CommonData.CART_URL);
        });
        step("Выбираем офис для сдачи анализов", () -> {
            desktop.clickElement(CartConstants.INVITRO_OFFICE_TITLE)
                    .clickElement(CartConstants.CHOOSING_MEDICAL_OFFICE_TITLE)
                    .clickElement(CartConstants.OFFICE_ADDRESS)
                    .clickElement(CartConstants.CHOOSING_OFFICE_TITLE)
                    .clickElement(CartConstants.ORDER_TITLE);
        });
        step("Проверяем, что мы находится на странице оплаты заказа", () -> {
            desktop.checkH1(CartConstants.ORDERING_TITLE);
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Выбор выезда на дом для сдачи анализов в корзине")
    @Description("Тест проверяет функциональность выбора выезда специалистов на дом и корректное отображение информации")
    void testChoosingHomeOrderInCart() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts()
                    .setCityCookie(ProductConstants.SEVASTOPOL_CITY_ID);
        });
        step("Переходим в корзину", () -> {
            desktop.openPage(CommonData.CART_URL);
        });
        step("Выбираем выезд на дом для сдачи анализов", () -> {
            desktop.clickElement(CartConstants.HOME_ORDER_TITLE);
        });
        step("Проверяем, что на странице отображаются элементы для вызова специалистов на дом", () -> {
            desktop.checkH2(CartConstants.UNAUTH_USER_INFO_ORDERING_TITLE)
                    .checkButtonState(CartConstants.ORDER_TITLE, CommonData.DISABLED);
            step("Проверяем цену на вызов бригады", () -> {
                desktop.checkProductInCart(
                        CartConstants.MEDICAL_TEAM_ORDER_TITLE,
                        ProductConstants.SEVASTOPOL_MEDICAL_TEAM_PRICE_TITLE);
            });
        });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tests.web.providers.CitiesPriceProvider#provideCitiesData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Проверка цен в корзине для города:")
    @Description("Тест проверяет корректность отображения цен на продукты в корзине для различных городов")
    void testPricesByCityInCart(CitiesInfo city, ProductsInfo productsInfo) {
        step("Подготавливаем исходные данные для корзины и города: " + city.getName(), () -> {
            cookie.setCartProducts()
                    .setCityCookie(city.getId());
        });
        step("Переходим в корзину", () -> {
            desktop.openPage(CART_URL);
        });
        step("Проверяем цены на продукты", () -> {
            desktop.checkProductInCart(productsInfo.getVen_title(), city.getVen_price())
                    .checkProductInCart(productsInfo.getObs158_title(), city.getObs158_price());
        });
    }

}
