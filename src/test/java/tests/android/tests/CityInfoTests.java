package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.android.config.PreRunConfig;
import tests.android.models.CityInfo;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.InvitroElementsPage;

import static io.qameta.allure.Allure.step;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("[Android] City Search Tests")
public class CityInfoTests extends PreRunConfig {

    private static final String UNKNOWN_CITY = "AQA",
            UNKNOWN_CITY_PLACEHOLDER_TEXT = "Город не найден";
    private final AndroidElementsPage android = new AndroidElementsPage();
    private final InvitroElementsPage invitro = new InvitroElementsPage();

    //TODO: Тест не будет работать удаленно из-за проблем с загрузкой дерева элементов в BrowserStack
    // Когда BrowserStack исправит эту проблему, можно будет запускать тест на прогон в BrowserStack
    @Test
    @Tag("only-local")
    @DisplayName("[Android] Проверка появления заглушки при поиске несуществующего города")
    @Description("Тест проверяет корректное отображение заглушки " +
            "'Город не найден' при поиске несуществующего города " +
            "в приложении")
    void unknownCityPlaceholderAppears() {
        step("Инициализация приложения", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Поиск несуществующего города", () -> {
            android.setSearchText(UNKNOWN_CITY);
        });
        step("Валидация отображения заглушки", () -> {
            invitro.verifyCityPlaceholder(UNKNOWN_CITY_PLACEHOLDER_TEXT);
        });
    }

    @ParameterizedTest(name = "[Android] Проверка отображения секций в меню для города {0}")
    @MethodSource("tests.android.providers.DataProvider#provideCitySectionsData")
    void testCityMenuSections(CityInfo cityInfo){
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выбираем город: " + cityInfo.getCityName(), () -> {
            invitro.selectCity(cityInfo.getCityName());
        });
        step("Отклоняем разрешение на геолокацию", () -> {
            android.locationPermissionDeny();
            //TODO: Используем координаты из-за проблем с загрузкой дерева элементов в BrowserStack
            // Когда BrowserStack исправит эту проблему, можно будет переписать на нормальные локаторы
            android.tapByCoordinates(993, 177);
        });
        step("Проверяем корректность отображения секций меню", () -> {
            invitro.verifySectionDisplayed(cityInfo.getSections().getAllResults());
            invitro.verifySectionDisplayed(cityInfo.getSections().getDynamicResults());
            invitro.verifySectionDisplayed(cityInfo.getSections().getMyRecords());
            invitro.verifySectionDisplayed(cityInfo.getSections().getMyOrders());
            invitro.verifySectionDisplayed(cityInfo.getSections().getAnalyses());
            invitro.verifySectionDisplayed(cityInfo.getSections().getMedicalOrder());
            invitro.verifySectionDisplayed(cityInfo.getSections().getAddresses());
            if (cityInfo.getSections().getHomeOrder() != null) {
                invitro.verifySectionDisplayed(cityInfo.getSections().getHomeOrder());
            }
        });
    }

}
