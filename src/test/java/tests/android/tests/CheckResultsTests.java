package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.android.config.AndroidConfig;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.InvitroElementsPage;
import constants.FakerConstants;

import static io.qameta.allure.Allure.step;
import static constants.IntConstants.MAX_LIMIT_ATTEMPTS;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("[Android]")
public class CheckResultsTests extends AndroidConfig {

    private final InvitroElementsPage invitro = new InvitroElementsPage();
    private final AndroidElementsPage android = new AndroidElementsPage();

    private final FakerConstants fakerConstants = new FakerConstants();

    private static final String CITY_VOLGOGRAD = "Волгоград";

    @Test
    @DisplayName("[Android] Блокировка проверки результатов анализов при частых запросах")
    @Description("Проверяем, что UI ограничивает количество запросов и блокирует пользователя при превышении лимита")
    void testCheckResultsRateLimitExceeded() {
        step("Подготовка приложения к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Настройка города и разрешений", () -> {
            invitro.selectCity(CITY_VOLGOGRAD);
            android.locationPermissionDeny();
        });
        step("Переход к форме проверки результатов", () -> {
            //TODO: Используем координаты из-за проблем с загрузкой дерева элементов в BrowserStack
            // Когда BrowserStack исправит эту проблему, можно будет переписать на нормальные локаторы
            android.tapByCoordinates(999, 170);
            invitro.selectCityMenuItem("Все результаты");
        });
        step("Ввод тестовых данных в форму", () -> {
            invitro.setInz(fakerConstants.inz)
                    .setBirthDate(fakerConstants.birthDateAndroid)
                    .setSurname(fakerConstants.lastName);
        });
        step("Тестирование ограничения количества попыток", () -> {
            for (int i = MAX_LIMIT_ATTEMPTS.getValue(); i > 0; i--) {
                String expectedEnding = invitro.getRemainingAttemptsText(i);
                String expectedError = String.format("По введенным данным результатов не найдено. %s", expectedEnding);
                invitro.acceptCheckResult()
                        .checkErrorText(expectedError);
            }
        });
        step("Проверка активации таймера ожидания", () -> {
            invitro.acceptCheckResult()
                    .checkCooldownTimerMessage();
        });
    }

}
