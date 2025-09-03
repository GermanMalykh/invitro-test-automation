package tests.web.tests;

import constants.CommonConstants;
import constants.UrlConstants;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.web.constants.CartConstants;
import tests.web.constants.ProductConstants;
import tests.web.base.PageManager;

import static io.qameta.allure.Allure.step;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("Web Tests")
public class CartConfigurationTest extends PageManager {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Выбор офиса для сдачи анализов и проверка деталей заказа")
    @Description("Тест проверяет процесс выбора медицинского офиса для сдачи анализов и переход к оформлению заказа")
    void choosingOfficeOrderInCartAndCheckOrderDetails() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts();
        });
        step("Переходим в корзину", () -> {
            base.openPage(UrlConstants.CART_URL);
        });
        step("Выбираем офис для сдачи анализов", () -> {
            base.clickByText(CartConstants.INVITRO_OFFICE_TITLE)
                    .clickByText(CartConstants.CHOOSING_MEDICAL_OFFICE_TITLE)
                    .clickByText(CartConstants.OFFICE_ADDRESS)
                    .clickByText(CartConstants.CHOOSING_OFFICE_TITLE);
        });
        step("Переходим к оформлению заказа", () -> {
            base.clickByText(CartConstants.ORDER_TITLE);
        });
        step("Проверяем детали на странице оплаты заказа", () -> {
            base.checkH1(CartConstants.ORDERING_TITLE);
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Выбор выезда на дом для сдачи анализов и проверка деталей заказа")
    @Description("Тест проверяет функциональность выбора выезда специалистов на дом и корректное отображение информации")
    void choosingHomeOrderInCartAndCheckOrderDetails() {
        step("Подготавливаем исходные данные для корзины", () -> {
            cookie.setCartProducts()
                    .setCityCookie(ProductConstants.SEVASTOPOL_CITY_ID);
        });
        step("Переходим в корзину", () -> {
            base.openPage(UrlConstants.CART_URL);
        });
        step("Выбираем выезд на дом для сдачи анализов", () -> {
            base.clickByText(CartConstants.HOME_ORDER_TITLE);
        });
        step("Проверяем, что на странице отображаются элементы для вызова специалистов на дом", () -> {
            base.checkH2(CartConstants.UNAUTH_USER_INFO_ORDERING_TITLE)
                    .checkButtonState(CartConstants.ORDER_TITLE, CommonConstants.DISABLED);
            step("Проверяем цену на вызов бригады", () -> {
                cart.checkProductPrice(
                        CartConstants.MEDICAL_TEAM_ORDER_TITLE,
                        ProductConstants.SEVASTOPOL_MEDICAL_TEAM_PRICE_TITLE);
            });
        });
    }

}
