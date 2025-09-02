package tests.android.tests;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.android.config.AndroidConfig;
import tests.android.constants.TestData;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.CartPage;
import tests.android.pages.invitro.InvitroElementsPage;
import tests.android.pages.invitro.MedicalTestElementsPage;
import tests.android.models.ProductsInfo;

import static io.qameta.allure.Allure.step;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("[Android]")
public class CartInfoTests extends AndroidConfig {

    private static final String COVID_19_CATEGORY_NAME = "COVID-19",
            COVID_19_TEST_NAME = "Для переболевших COVID-19. Витамины и минералы " +
                    "(For recovered from COVID-19. Vitamins and minerals)";

    private final CartPage cart = new CartPage();
    private final AndroidElementsPage android = new AndroidElementsPage();
    private final InvitroElementsPage invitro = new InvitroElementsPage();
    private final MedicalTestElementsPage test = new MedicalTestElementsPage();

    @ParameterizedTest(name = "[Android] Проверка информации в корзине для города {0}")
    @MethodSource("tests.android.providers.DataProvider#provideCartData")
    void testAddProductInCartAndCheckInfo(ProductsInfo productsInfo) {
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выбираем город: " + productsInfo.getCityName(), () -> {
            invitro.selectCity(productsInfo.getCityName());
        });
        step("Отклоняем разрешение на геолокацию", () -> {
            android.locationPermissionDeny();
        });
        step("Переходим к выбору анализов", () -> {
            //TODO: Используем координаты из-за проблем с загрузкой дерева элементов в BrowserStack
            // Когда BrowserStack исправит эту проблему, можно будет переписать на нормальные локаторы
            android.tapByCoordinates(999, 170);
            invitro.selectCityMenuItem("Каталог анализов");
        });
        step("Выбираем категорию анализов и добавляем товар в корзину", () -> {
            test.selectCategory("Комплексы анализов")
                    .selectItem(COVID_19_CATEGORY_NAME)
                    .checkItemsTitle(COVID_19_CATEGORY_NAME)
                    .selectTest(COVID_19_TEST_NAME)
                    .checkItemsName(COVID_19_TEST_NAME)
                    .addItemToCart()
                    .checkButtonCart();
        });
        step("Закрываем тулбар и переходим в корзину", () -> {
            invitro.closeToolbar()
                    .navigateToBasket();
        });
        step("Проверяем информацию о первом товаре в корзине", () -> {
            cart.checkProductName(productsInfo.getItems()[0].getName())
                    .checkProductPrice(productsInfo.getItems()[0].getPrice())
                    .checkProductNumber(productsInfo.getItems()[0].getNumber());
        });
        step("Проверяем информацию о втором товаре в корзине", () -> {
            cart.scrollAndCheckProductName(productsInfo.getItems()[1].getName())
                    .checkProductPrice(productsInfo.getItems()[1].getPrice())
                    .checkProductNumber(productsInfo.getItems()[1].getNumber());
        });
        step("Проверяем итоговую цену в корзине", () -> {
            cart.checkTotalPrice(productsInfo.getTotalPrice());
        });
    }

    @Test
    @DisplayName("[Android] Смена офиса и проверка изменений")
    void testChangeOffice() {
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выбираем город: " + TestData.VOLGOGRAD, () -> {
            invitro.selectCity(TestData.VOLGOGRAD);
        });
        step("Отклоняем разрешение на геолокацию", () -> {
            android.locationPermissionDeny();
        });
        step("Переходим к выбору анализов", () -> {
            //TODO: Используем координаты из-за проблем с загрузкой дерева элементов в BrowserStack
            // Когда BrowserStack исправит эту проблему, можно будет переписать на нормальные локаторы
            android.tapByCoordinates(999, 170);
            invitro.selectCityMenuItem("Каталог анализов");
        });
        step("Выбираем категорию анализов и добавляем товар в корзину", () -> {
            test.selectCategory("Комплексы анализов")
                    .selectItem(COVID_19_CATEGORY_NAME)
                    .selectTest(COVID_19_TEST_NAME)
                    .addItemToCart();
        });
        step("Закрываем тулбар и переходим в корзину", () -> {
            invitro.closeToolbar()
                    .navigateToBasket();
        });
        cart.changeOfficeButton()
                .choseOfficeByList("ул. Невская")
                .checkOfficeProperty();
        android.tapByCoordinates(540, 2000);
        cart.checkAddress("ул. Невская");
    }
}
