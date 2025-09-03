package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.android.base.PageManager;
import tests.android.constants.TestData;
import tests.android.models.ProductsInfo;

import static constants.CommonConstants.COVID_19_CATEGORY;
import static constants.CommonConstants.COVID_19_TEST_NAME;
import static io.qameta.allure.Allure.step;
import static tests.android.constants.TestData.OGAREVA_OFFICE_ADDRESS;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("Android Tests")
public class CartManagementTests extends PageManager {

    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Android] Проверка информации в корзине для города:")
    @ParameterizedTest(name = "{0}")
    @Description("Тест проверяет корректность добавления товаров в корзину " +
            "и отображения информации о товарах (название, цена, количество) " +
            "для различных городов")
    @MethodSource("tests.android.providers.DataProvider#provideCartData")
    void verifyCartProductInfo(ProductsInfo productsInfo) {
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
            invitro.closeAuthScreen()
                    .selectCityMenuItem("Каталог анализов");
        });
        step("Выбираем категорию анализов и добавляем товар в корзину", () -> {
            medicalTest.selectCategory("Комплексы анализов")
                    .selectItem(COVID_19_CATEGORY)
                    .checkItemsTitle(COVID_19_CATEGORY)
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
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[Android] Смена офиса и проверка изменений")
    @Description("Тест проверяет функциональность смены офиса в корзине " +
            "и корректность отображения выбранного адреса")
    void verifyOfficeAddressChange() {
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
            invitro.closeAuthScreen()
                    .selectCityMenuItem("Каталог анализов");
        });
        step("Выбираем категорию анализов и добавляем товар в корзину", () -> {
            medicalTest.selectCategory("Комплексы анализов")
                    .selectItem(COVID_19_CATEGORY)
                    .selectTest(COVID_19_TEST_NAME)
                    .addItemToCart();
        });
        step("Закрываем тулбар и переходим в корзину", () -> {
            invitro.closeToolbar()
                    .navigateToBasket();
        });
        step("Выбираем офис", () -> {
            cart.changeOfficeButton()
                    .chooseOfficeByList(OGAREVA_OFFICE_ADDRESS)
                    .checkOfficeProperty()
                    .chooseOfficeButton();
        });
        step("Проверяем адрес выбранного офиса", () -> {
            cart.checkAddress(OGAREVA_OFFICE_ADDRESS);
        });
    }
}
