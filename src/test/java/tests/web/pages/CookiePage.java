package tests.web.pages;

import constants.UrlConstants;
import io.qameta.allure.Step;
import constants.FilePathConstants;
import helpers.JsonConverter;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CookiePage {

    @Step("Добавляем куку с данными о продуктах")
    public CookiePage setCartProducts() {
        String encodedCart = JsonConverter.readCompactEncodedJson(FilePathConstants.CART_PRODUCT_JSON_WEB);
        open(UrlConstants.COOKIE_CONFIG_URL);

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
    public CookiePage setCityCookie(String cityId) {
        open(UrlConstants.COOKIE_CONFIG_URL);

        String cityScript = String.format(
                "document.cookie = 'INVITRO_CITY_LK_GUID=%s; path=/; domain=.invitro.ru';",
                cityId
        );
        executeJavaScript(cityScript);
        return this;
    }

    @Step("Закрываем cookie-баннер и всплывающие окна")
    public CookiePage closeCookieBanner() {
        executeJavaScript("document.querySelectorAll(" +
                "'.cookie--popup, .attention--page, .attention--fixed').forEach(e => e.remove());");
        return this;
    }

}
