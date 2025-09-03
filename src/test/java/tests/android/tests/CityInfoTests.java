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
import tests.android.models.CityInfo;

import static constants.CommonConstants.UNKNOWN_CITY;
import static constants.CommonConstants.UNKNOWN_CITY_PLACEHOLDER_TEXT;
import static io.qameta.allure.Allure.step;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("Android Tests")
public class CityInfoTests extends PageManager {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[Android] Проверка появления заглушки при поиске несуществующего города")
    @Description("Тест проверяет корректное отображение заглушки 'Город не найден' в приложении")
    void verifyCityNotFoundPlaceholder() {
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выполняем поиск несуществующего города", () -> {
            android.setSearchText(UNKNOWN_CITY);
        });
        step("Проверяем отображение заглушки", () -> {
            invitro.checkCityPlaceholder(UNKNOWN_CITY_PLACEHOLDER_TEXT);
        });
    }

    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[Android] Проверка отображения секций в меню для города:")
    @ParameterizedTest(name = "{0}")
    @Description("Тест проверяет корректное отображение всех секций меню для различных городов")
    @MethodSource("tests.android.providers.DataProvider#provideCitySectionsData")
    void verifyCityMenuSections(CityInfo cityInfo) {
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выбираем город: " + cityInfo.getCityName(), () -> {
            invitro.selectCity(cityInfo.getCityName());
        });
        step("Отклоняем разрешение на геолокацию", () -> {
            android.locationPermissionDeny();
        });
        step("Проверяем корректность отображения секций меню", () -> {
            invitro.checkSectionDisplayed(cityInfo.getSections().getAllResults());
            invitro.checkSectionDisplayed(cityInfo.getSections().getDynamicResults());
            invitro.checkSectionDisplayed(cityInfo.getSections().getMyRecords());
            invitro.checkSectionDisplayed(cityInfo.getSections().getMyOrders());
            invitro.checkSectionDisplayed(cityInfo.getSections().getAnalyses());
            invitro.checkSectionDisplayed(cityInfo.getSections().getMedicalOrder());
            invitro.checkSectionDisplayed(cityInfo.getSections().getAddresses());
            if (cityInfo.getSections().getHomeOrder() != null) {
                invitro.checkSectionDisplayed(cityInfo.getSections().getHomeOrder());
            }
        });
    }

}
