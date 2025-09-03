package tests.web.pages;

import constants.UrlConstants;
import io.qameta.allure.Step;
import constants.FilePathConstants;
import helpers.JsonConverter;
import tests.web.constants.CookieConstants;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CookiePage {

    @Step("Добавляем куку с данными города и продукта")
    public CookiePage fillingCartInfo(String cityId) {
        setCartProducts();
        setCityGuid(cityId);
        return this;
    }

    @Step("Добавляем куку с данными о продуктах")
    public CookiePage setCartProducts() {
        String encodedCart = JsonConverter.readCompactEncodedJson(FilePathConstants.CART_PRODUCT_JSON_WEB);
        open(UrlConstants.COOKIE_CONFIG_URL);
        String productInCartScript = String.format(
                "document.cookie = '%s=%s; path=%s; domain=%s';",
                CookieConstants.INVITRO_CART_COOKIE,
                encodedCart,
                CookieConstants.COOKIE_PATH,
                CookieConstants.COOKIE_DOMAIN
        );
        executeJavaScript(productInCartScript);
        return this;
    }

    @Step("Добавляем куку с данными города")
    public CookiePage setCityGuid(String cityId) {
        open(UrlConstants.COOKIE_CONFIG_URL);
        String cityScript = String.format(
                "document.cookie = '%s=%s; path=%s; domain=%s';",
                CookieConstants.INVITRO_CITY_LK_GUID_COOKIE,
                cityId,
                CookieConstants.COOKIE_PATH,
                CookieConstants.COOKIE_DOMAIN
        );
        executeJavaScript(cityScript);
        return this;
    }

    @Step("Закрываем cookie-баннер и всплывающие окна")
    public CookiePage closeCookieBanner() {
        executeJavaScript("document.querySelectorAll('"
                + CookieConstants.COOKIE_BANNER_SELECTOR +
                "').forEach(e => e.remove());");
        return this;
    }

}
