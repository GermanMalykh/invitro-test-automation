package tests.web.base;

import tests.web.config.WebConfig;
import tests.web.pages.BasePage;
import tests.web.pages.CartPage;
import tests.web.pages.CatalogPage;
import tests.web.pages.CookiePage;
import tests.web.pages.MainPage;

public class PageManager extends WebConfig {
    protected BasePage base = new BasePage();
    protected MainPage main = new MainPage();
    protected CookiePage cookie = new CookiePage();
    protected CartPage cart = new CartPage();
    protected CatalogPage catalog = new CatalogPage();
}
