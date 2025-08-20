package tests.web.tests;

import org.junit.jupiter.api.Test;
import tests.web.configs.WebConfig;
import tests.web.pages.BannersPage;
import tests.web.pages.DesktopBasePage;

public class WebTests extends WebConfig {

    DesktopBasePage dp = new DesktopBasePage();
    BannersPage bp = new BannersPage();

    String MENU_CATEGORY = "Анализы",
            PRODUCT_CATEGORY = "COVID-19",
            BASE_URL = "https://invitro.ru/",
            PRODUCT_TITLE = "Контрольное исследование после перенесенной острой респираторной вирусной инфекции",
            SIDEBAR_MENU_CATEGORY = "Комплексы анализов",
            PAGE_NUMBER_2 = "2";

    // TODO: Автоматически извлекать цену из карточки товара
    // TODO: Добавить проверку цены в корзине
    // TODO: Добавить проверку количества товара в корзине (должно быть 1)
    // TODO: Добавить проверку артикула товара (№ ОБС158)
    // TODO: Добавить очистку корзины после теста

    @Test
    void testAddAndCheckProductInfoInCart() {
        dp.openPageByURL(BASE_URL);
        bp.closePromoBanner();
        dp.selectHeaderMainMenuTitleDesktop(MENU_CATEGORY)
                .clickByElement(dp.selectElementByParent(SIDEBAR_MENU_CATEGORY))
                .selectElement(PRODUCT_CATEGORY);
        bp.closeCookieBanner().closePromoBanner();
        dp.selectPage(PAGE_NUMBER_2);
        bp.closePromoBanner();
        dp.findProductOnPAge(PRODUCT_TITLE);
        bp.closePromoBanner();
        dp.addProductToCart()
                .checkProductNameInCart(PRODUCT_TITLE);
    }

}
