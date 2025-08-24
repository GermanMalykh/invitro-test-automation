package tests.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.ErrorMessages;
import tests.api.constants.Errors;
import tests.constants.FakerConstants;
import tests.api.models.ErrorResponse;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.constants.IntConstants.MAX_LIMIT_ATTEMPTS;
import static tests.api.constants.TestData.INVALID_BIRTHDAY;
import static tests.api.constants.TestData.INVALID_INZ;
import static tests.api.constants.TestData.INVALID_LAST_NAME;

@Tag("api")
@Owner("germanmalykh")
@DisplayName("[API] Unauth Results API Tests")
public class UnauthResultsApiTest extends ApiConfig {

    protected ValidatableResponse response;

    private final InvitroApiClient apiClient = new InvitroApiClient();

    private final FakerConstants fakerConstants = new FakerConstants();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Получение результатов анализов по данным пациента, которого нет в системе")
    @Description("Проверяем, что API корректно обрабатывает запросы с данными несуществующего пациента")
    void testLabResultsNotFoundPatientData() {
        step("Выполняем запрос на получение результатов анализов", () -> {
            response = apiClient.getResultsInfo(
                    fakerConstants.birthDate,
                    fakerConstants.inz,
                    fakerConstants.lastName);
        });
        step("Проверяем, что статус код ответа: \"409\"", () -> {
            response.statusCode(409);
        });
        step("Проверяем, что API возвращает ошибку конфликта проверки данных", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError()).isEqualTo(Errors.CONFLICT.getValue());
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Блокировка проверки результатов анализов при частых запросах")
    @Description("Проверяем, что API корректно ограничивает количество запросов и блокирует пользователя при превышении лимита")
    void testLabResultsRateLimitExceeded() {
        step("Выполняем запрос на получение результатов анализов \"" + MAX_LIMIT_ATTEMPTS.getValue() + "\" раза", () -> {
            for (int i = 0; i < MAX_LIMIT_ATTEMPTS.getValue(); i++) {
                apiClient.getResultsInfo(
                                fakerConstants.birthDate,
                                fakerConstants.inz,
                                fakerConstants.lastName)
                        .statusCode(409);
            }
        });
        step("Выполняем запрос на получение результатов анализов, превышая лимит проверок", () -> {
            response = apiClient.getResultsInfo(
                    fakerConstants.birthDate,
                    fakerConstants.inz,
                    fakerConstants.lastName);
        });
        step("Проверяем, что статус код ответа: \"429\"", () -> {
            response.statusCode(429);
        });
        step("Проверяем, что ответ содержит ошибку множественных запросов при проверки анализов", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError())
                    .isEqualTo(Errors.EXCEEDED_REQUESTS.getValue());
            assertThat(errorResponse.getMessage())
                    .isEqualTo(ErrorMessages.EXCEEDED_MESSAGE.getValue());
            assertThat(errorResponse.getAttempts())
                    .isNull();
        });
    }

    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Получение результатов анализов с передачей пустого значения для параметров")
    @ParameterizedTest
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    @Description("Проверяем, что API корректно валидирует null значения для всех обязательных параметров")
    void testNullParametersValidation(String nullParameter) {
        step("Выполняем запрос на получение результатов анализов с пустым значением параметра: '" + nullParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(nullParameter) ? null : fakerConstants.birthDate,
                    "inz".equals(nullParameter) ? null : fakerConstants.inz,
                    "lastName".equals(nullParameter) ? null : fakerConstants.lastName
            );
        });
        step("Статус код ответа \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации пустого значения для параметра: " + nullParameter, () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError())
                    .isEqualTo(Errors.BAD_REQUEST.getValue());
            assertThat(errorResponse.getMessage())
                    .isEqualTo(ErrorMessages.BAD_REQUEST_MESSAGE.getValue());
        });
    }

    @Tag("server-bug")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Получение результатов анализов с невалидными данными параметров")
    @ParameterizedTest
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    @Description("Проверяем, что API корректно обрабатывает невалидные форматы данных для всех параметров")
    void testInvalidDataFormats(String invalidParameter) {
        step("Выполняем запрос на получение результатов анализов с невалидным значением параметра: '" + invalidParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(invalidParameter) ? INVALID_BIRTHDAY.getValue() : fakerConstants.birthDate,
                    "inz".equals(invalidParameter) ? INVALID_INZ.getValue() : fakerConstants.inz,
                    "lastName".equals(invalidParameter) ? INVALID_LAST_NAME.getValue() : fakerConstants.lastName
            );
        });
        step("Проверяем, что API возвращает ошибку валидации невалидного значения для параметра: '" + invalidParameter, () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            if ("birthDate".equals(invalidParameter)) {
                response.statusCode(400);
                assertThat(errorResponse.getError())
                        .isEqualTo(Errors.BAD_REQUEST.getValue());
            } else if ("inz".equals(invalidParameter)) {
                response.statusCode(500);
                assertThat(errorResponse.getError())
                        .isEqualTo(Errors.INTERNAL_ERROR.getValue());
            } else if ("lastName".equals(invalidParameter)) {
                response.statusCode(409);
                assertThat(errorResponse.getError())
                        .isEqualTo(Errors.CONFLICT.getValue());
                assertThat(errorResponse.getAttempts())
                        .isEqualTo(MAX_LIMIT_ATTEMPTS.getValue());
            }
        });
    }
}
