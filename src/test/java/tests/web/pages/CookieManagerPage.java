package tests.web.pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import constants.FilePathConstants;
import helpers.JsonConverter;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CookieManagerPage {

    private static final String CART_ITEMS_SELECTOR = ".analyzes-item";
    private static final String PRE_CONFIG_URL = "https://lk3.invitro.ru/assets/edna-banner-close.svg";

    public static ElementsCollection CART_ITEMS = $$(CART_ITEMS_SELECTOR);

    @Step("Добавляем куку c данными о продуктах")
    public CookieManagerPage setCartProducts() {
        String encodedCart = JsonConverter.readCompactEncodedJson(FilePathConstants.CART_PRODUCT_JSON_WEB);

        open(PRE_CONFIG_URL);

        String productInCartScript = String.format(
                "document.cookie = 'INVITRO_CART=%s; path=/; domain=.invitro.ru';",
                encodedCart
        );
        executeJavaScript(productInCartScript);

        String cityScript = "document.cookie = 'INVITRO_CITY_LK_GUID=9e4c02c6-274b-41e9-80bb-7f5d3359a463; path=/; domain=.invitro.ru';";
        executeJavaScript(cityScript);

        return this;
    }

    @Step("Добавляем куку с данными города")
    public CookieManagerPage setCityCookie(String cityId) {
        open(PRE_CONFIG_URL);

        String cityScript = String.format(
                "document.cookie = 'INVITRO_CITY_LK_GUID=%s; path=/; domain=.invitro.ru';",
                cityId
        );

        executeJavaScript(cityScript);
        return this;
    }

}
