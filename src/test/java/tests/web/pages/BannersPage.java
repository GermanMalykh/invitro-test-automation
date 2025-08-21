package tests.web.pages;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BannersPage {

    public BannersPage closeCookie() {
        executeJavaScript("document.querySelectorAll(" +
                "'.cookie--popup, .attention--page, .attention--fixed').forEach(e => e.remove());");
        return this;
    }
}
