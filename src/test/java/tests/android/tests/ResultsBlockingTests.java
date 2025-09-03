package tests.android.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.android.base.PageManager;

import static constants.CommonConstants.CITY_VOLGOGRAD;
import static io.qameta.allure.Allure.step;
import static constants.IntConstants.MAX_LIMIT_ATTEMPTS;

@Tag("android")
@Owner("germanmalykh")
@DisplayName("Android Tests")
public class ResultsBlockingTests extends PageManager {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("[Android] Блокировка проверки результатов анализов при частых запросах")
    @Description("Проверяем, что UI ограничивает количество запросов и блокирует пользователя при превышении лимита")
    void verifyResultsBlocking() {
        step("Подготавливаем приложение к работе", () -> {
            invitro.waitForLoaderToDisappear();
            invitro.closeToolbar();
        });
        step("Выбираем город и настраиваем разрешения в приложении", () -> {
            invitro.selectCity(CITY_VOLGOGRAD);
            android.locationPermissionDeny();
        });
        step("Переходим к форме проверки результатов", () -> {
            invitro.closeAuthScreen()
                    .selectCityMenuItem("Все результаты");
        });
        step("Вводим тестовые данные в форму", () -> {
            invitro.setInz(fakerConstants.inz)
                    .setBirthDate(fakerConstants.birthDateAndroid)
                    .setSurname(fakerConstants.lastName);
        });
        step("Проверяем текст ошибки при повторных попытках проверки анализов", () -> {
            for (int i = MAX_LIMIT_ATTEMPTS.getValue(); i > 0; i--) {
                invitro.acceptCheckResult()
                        .checkErrorTextForAttempt(i);
            }
        });
        step("Проверяем отображение таймера ожидания", () -> {
            invitro.acceptCheckResult()
                    .checkCooldownTimerMessage();
        });
    }

}
