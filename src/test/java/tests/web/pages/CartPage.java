package tests.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CartPage {

    public static ElementsCollection CART_ITEMS = $$(".analyzes-item");

    @Step("Добавляем куку c данными о продуктах в корзине")
    public CartPage addingProductsCookie() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String cartJsonRaw = Files.readString(Paths.get("src/test/resources/jsons/cartJson.json"));
        String cartJson = mapper.writeValueAsString(mapper.readTree(cartJsonRaw));
        String encodedCart = java.net.URLEncoder.encode(cartJson, java.nio.charset.StandardCharsets.UTF_8);

        open("https://lk3.invitro.ru/assets/edna-banner-close.svg");
        String productInCartScript = String.format(
                "document.cookie = 'INVITRO_CART=%s; path=/; domain=.invitro.ru';",
                encodedCart
        );
        executeJavaScript(productInCartScript);

        String cityScript = "document.cookie = 'INVITRO_CITY_LK_GUID=9e4c02c6-274b-41e9-80bb-7f5d3359a463; path=/; domain=.invitro.ru';";
        executeJavaScript(cityScript);

        return this;
    }

}
