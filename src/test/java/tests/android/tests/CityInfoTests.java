package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.android.config.PreRunConfig;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.InvitroElementsPage;

import static io.qameta.allure.Allure.step;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("[Android] Base Android Tests")
public class CityInfoTests extends PreRunConfig {

    String UNKNOWN_CITY = "unknown",
            UNKNOWN_CITY_PLACEHOLDER_TEXT = "Город не найден";
    AndroidElementsPage android = new AndroidElementsPage();
    InvitroElementsPage invitro = new InvitroElementsPage();

    @Test
    @Owner("germanmalykh")
    @DisplayName("[Android] Проверка появления заглушки при поиске несуществующего города")
    @Description("Тест проверяет корректное отображение заглушки " +
            "'Город не найден' при поиске несуществующего города " +
            "в приложении")
    void unknownCityPlaceholderAppears() {
        step("Ожидание исчезновения лоадера", () -> {
            invitro.waitForLoaderToDisappear();
        });
        step("Закрытие тулбара по клику на крестик", () -> {
            invitro.closeToolbar();
        });
        step("Поиск города: " + UNKNOWN_CITY, () -> {
            android.setSearchingText(UNKNOWN_CITY);
        });
        step("Проверка появления заглушки 'Город не найден'", () -> {
            invitro.verifyCityPlaceholder(UNKNOWN_CITY_PLACEHOLDER_TEXT);
        });
    }

}
