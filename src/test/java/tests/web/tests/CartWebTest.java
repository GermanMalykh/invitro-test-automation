package tests.web.tests;

import com.codeborne.selenide.Condition;
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

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
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
    void testChoosingOfficeInCart() throws IOException {
        cookie.setProducts();
        open("https://lk3.invitro.ru/cart");
        $(byText("В ИНВИТРО")).click();
        $(byText("Выбрать медицинский офис")).click();
        $(byText("Севастополь, ул. Адмирала Октябрьского, д. 8")).shouldBe(visible).click();
        $(byText("Выбрать офис")).click();
        $(byText("Оформить заказ")).click();
        $("h1").shouldHave(Condition.text("Оформление заказа"));
    }

    @Test
    void testChoosingHomeOrderInCart() throws IOException {
        cookie.setProducts();
        open("https://lk3.invitro.ru/cart");
        $(byText("Выезд на дом")).click();
        $("h2").shouldHave(Condition.text("Войдите или зарегистрируйтесь, чтобы оформить заказ"));
        $$("[class*='Button_button']")
                .findBy(Condition.exactText("Оформить заказ"))
                .shouldHave(Condition.attribute("disabled"));
        desktop.checkProductInCart("Выезд процедурной бригады", "400 ₽");
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
