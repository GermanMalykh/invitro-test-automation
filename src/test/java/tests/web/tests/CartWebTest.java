package tests.web.tests;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.web.configs.WebConfig;
import tests.web.constants.TestData;
import tests.web.models.CitiesInfo;
import tests.web.models.ProductsInfo;
import tests.web.pages.CookiePage;
import tests.web.pages.DesktopMainPage;

import static io.qameta.allure.Allure.step;
import static tests.web.constants.TestData.CART_URL;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("Cart Web Tests")
public class CartWebTest extends WebConfig {

    DesktopMainPage desktop = new DesktopMainPage();
    CookiePage cookie = new CookiePage();

    @Test
    @DisplayName("Очистка корзины и проверка её состояния")
    void testCartClean() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setProducts();
        });
        step("Переходим в корзину", () -> {
            desktop.openDesiredPage(TestData.CART_URL);
        });
        step("Очищаем корзину и проверяем её состояние", () -> {
            desktop.selectElement(TestData.CLEAN_CERT);
            desktop.propertyCheckH1(TestData.EMPTY_CART);
        });
    }

    @Test
    void testChoosingOfficeInCart() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setProducts();
        });
        step("Переходим в корзину", () -> {
            desktop.openDesiredPage(TestData.CART_URL);
        });
        step("Выбираем офис для сдачи анализов", () -> {
            desktop.selectElement(TestData.INVITRO_OFFICE_TITLE)
                    .selectElement(TestData.CHOOSING_MEDICAL_OFFICE_TITLE)
                    .selectElement(TestData.OFFICE_ADDRESS)
                    .selectElement(TestData.CHOOSING_OFFICE_TITLE)
                    .selectElement(TestData.ORDER_TITLE);
        });
        step("Проверяем, что мы находится на странице оплаты заказа", () -> {
            desktop.propertyCheckH1(TestData.ORDERING_TITLE);
        });
    }

    @Test
    void testChoosingHomeOrderInCart() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setProducts()
                    .setCity(TestData.SEVASTOPOL_CITY_ID);
        });
        step("Переходим в корзину", () -> {
            desktop.openDesiredPage(TestData.CART_URL);
        });
        step("Выбираем выезд на дом для сдачи анализов", () -> {
            desktop.selectElement(TestData.HOME_ORDER_TITLE);
        });
        step("Проверяем, что на странице отображаются элементы для вызова специалистов на дом", () -> {
            desktop.propertyCheckH2(TestData.UNAUTH_USER_INFO_ORDERING_TITLE)
                    .buttonCondition(TestData.ORDER_TITLE, TestData.DISABLED);
            step("Проверяем цену на вызов бригады", () -> {
                desktop.checkProductInCart(
                        TestData.MEDICAL_TEAM_ORDER_TITLE,
                        TestData.SEVASTOPOL_MEDICAL_TEAM_PRICE_TITLE);
            });
        });
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tests.web.providers.CitiesPriceProvider#provideCitiesData")
    @DisplayName("Проверка цен в корзине для города:")
    void testPricesByCityInCart(CitiesInfo city, ProductsInfo productsInfo) {
        step("Подготавливаем исходные данные для корзины и города: " + city.getName(), () -> {
            cookie.setProducts()
                    .setCity(city.getId());
        });
        step("Переходим в корзину", () -> {
            desktop.openDesiredPage(CART_URL);
        });
        step("Проверяем цены на продукты", () -> {
            desktop.checkProductInCart(productsInfo.getVen_title(), city.getVen_price())
                    .checkProductInCart(productsInfo.getObs158_title(), city.getObs158_price());
        });
    }

}
