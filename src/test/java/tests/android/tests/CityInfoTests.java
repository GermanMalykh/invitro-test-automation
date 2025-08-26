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
@DisplayName("[Android] City Search Tests")
public class CityInfoTests extends PreRunConfig {

    private static final String UNKNOWN_CITY = "AQA",
            UNKNOWN_CITY_PLACEHOLDER_TEXT = "Город не найден";
    private final AndroidElementsPage android = new AndroidElementsPage();
    private final InvitroElementsPage invitro = new InvitroElementsPage();

    @Test
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

}
