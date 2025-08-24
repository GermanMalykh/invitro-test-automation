package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.android.config.PreRunConfig;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.InvitroElementsPage;
import tests.constants.FakerConstants;

import static io.qameta.allure.Allure.step;
import static tests.constants.IntConstants.MAX_LIMIT_ATTEMPTS;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("[Android] Check Results Tests")
public class CheckResultsTests extends PreRunConfig {

    InvitroElementsPage invitro = new InvitroElementsPage();
    AndroidElementsPage android = new AndroidElementsPage();

    private final FakerConstants fakerConstants = new FakerConstants();

    String VOLGOGRAD = "Волгоград";

    @Test
    @DisplayName("[Android] Блокировка проверки результатов анализов при частых запросах")
    @Description("Проверяем, что UI ограничивает количество запросов и блокирует пользователя при превышении лимита")
    void testCheckResultsRateLimitExceeded() {
        step("Ожидание исчезновения лоадера", () -> {
            invitro.waitForLoaderToDisappear();
        });
        step("Закрытие тулбара по клику на крестик", () -> {
            invitro.closeToolbar();
        });
        step("Выбор города Волгоград", () -> {
            invitro.selectCity(VOLGOGRAD);
        });
        step("Отклонение разрешения на геолокацию", () -> {
            android.locationPermissionDeny();
        });
        step("Закрытие экрана авторизации и выбор пункта меню 'Все результаты'", () -> {
            invitro.closeAuthScreen()
                    .selectCityMenuItem("Все результаты");
        });
        step("Заполнение формы данными для проверки результатов", () -> {
            invitro.setInz(fakerConstants.inz)
                    .setBirthDate(fakerConstants.birthDateAndroid)
                    .setSurname(fakerConstants.lastName);
        });
        step("Проверка ограничения количества попыток", () -> {
            for (int i = MAX_LIMIT_ATTEMPTS.getValue(); i > 0; i--) {
                String expectedEnding = invitro.getRemainingAttemptsText(i);
                String expectedError = String.format("По введенным данным результатов не найдено. %s", expectedEnding);
                invitro.acceptCheckResult()
                        .checkErrorText(expectedError);
            }
        });
        step("Проверка сообщения о таймере ожидания после превышения лимита", () -> {
            invitro.acceptCheckResult()
                    .cooldownTimerMessage();
        });
    }

}
