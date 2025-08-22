package tests.web.pages;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CookieBannerPage {

    @Step("Закрываем cookie-баннер и всплывающие окна")
    public CookieBannerPage closeCookieBanner() {
        executeJavaScript("document.querySelectorAll(" +
                "'.cookie--popup, .attention--page, .attention--fixed').forEach(e => e.remove());");
        return this;
    }
}
