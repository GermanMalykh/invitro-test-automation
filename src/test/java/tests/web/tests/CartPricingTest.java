package tests.web.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.web.constants.CommonData;
import tests.web.models.CitiesInfo;
import tests.web.models.ProductsInfo;
import tests.web.base.CartPageManager;

import static io.qameta.allure.Allure.step;

@Tag("web")
@Owner("germanmalykh")
@DisplayName("Web Tests")
public class CartPricingTest extends CartPageManager {

    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Web] Проверка цен на продукты в корзине для города:")
    @ParameterizedTest(name = "{0}")
    @Description("Тест проверяет корректность отображения цен на продукты в корзине для различных городов")
    @MethodSource("tests.web.providers.CitiesPriceProvider#provideCitiesData")
    void pricesByCityInCart(CitiesInfo city, ProductsInfo productsInfo) {
        step("Подготавливаем исходные данные для корзины города: " + city.getName(), () -> {
            cookie.setCartProducts()
                    .setCityCookie(city.getId());
        });
        step("Переходим в корзину", () -> {
            base.openPage(CommonData.CART_URL);
        });
        step("Проверяем цены на продукты в корзине", () -> {
            cart.checkProductPrice(productsInfo.getVenTitle(), city.getVenPrice())
                    .checkProductPrice(productsInfo.getObs158Title(), city.getObs158Price());
        });
    }

}
