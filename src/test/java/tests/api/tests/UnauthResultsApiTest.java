package tests.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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
import tests.api.constants.FakerTestData;
import tests.api.models.ErrorResponse;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.constants.IntConstants.MAX_LIMIT_ATTEMPTS;
import static tests.api.constants.TestData.INVALID_BIRTHDAY;
import static tests.api.constants.TestData.INVALID_INZ;
import static tests.api.constants.TestData.INVALID_LAST_NAME;

@Epic("API Testing")
@Feature("Unauthorized Results API")
@Owner("germanmalykh")
@DisplayName("API тестирование: Получение результатов анализов (неавторизованный доступ)")
public class UnauthResultsApiTest extends ApiConfig {

    protected ValidatableResponse response;
    private final static InvitroApiClient apiClient = new InvitroApiClient();
    private final static FakerTestData fakerTestData = new FakerTestData();

    @Test
    @DisplayName("Получение результатов анализов по данным пациента, которого нет в системе")
    @Story("Patient Data Validation")
    @Description("Проверяем, что API корректно обрабатывает запросы с данными несуществующего пациента")
    @Severity(SeverityLevel.CRITICAL)
    void testLabResultsNotFoundPatientData() {
        step("Получаем результаты анализов", () -> {
            response = apiClient.getResultsInfo(
                    fakerTestData.birthDate,
                    fakerTestData.inz,
                    fakerTestData.lastName);
        });
        step("Проверяем, что статус код ответа: \"409\"", () -> {
            response.statusCode(409);
        });
        step("Проверяем, что ответ содержит ошибку конфликта проверки данных", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError())
                .as("Тип ошибки должен быть " + Errors.CONFLICT.getValue() + " для отсутствующего пациента")
                .isEqualTo(Errors.CONFLICT.getValue());
            assertThat(errorResponse.getMessage())
                .as("Сообщение об ошибке должно быть: " + ErrorMessages.EMPTY_MESSAGE.getValue())
                .isEqualTo(ErrorMessages.EMPTY_MESSAGE.getValue());
            assertThat(errorResponse.getAttempts())
                .as("Количество попыток должно быть равно лимиту: " + MAX_LIMIT_ATTEMPTS.getValue())
                .isEqualTo(MAX_LIMIT_ATTEMPTS.getValue());
        });
    }

    @Test
    @DisplayName("Блокировка проверки результатов при частых запросах")
    @Story("Rate Limiting")
    @Description("Проверяем, что API корректно ограничивает количество запросов и блокирует пользователя при превышении лимита")
    @Severity(SeverityLevel.CRITICAL)
    void testLabResultsRateLimitExceeded() {
        String birthDate = fakerTestData.birthDate;
        String inz = fakerTestData.inz;
        String lastName = fakerTestData.lastName;
        step("Получаем результаты анализов несколько раз подряд", () -> {
            for (int i = 0; i < MAX_LIMIT_ATTEMPTS.getValue(); i++) {
                apiClient.getResultsInfo(birthDate, inz, lastName);
            }
        });
        step("Получаем результаты анализов и проверяем статус код ответа \"429\"", () -> {
            response = apiClient.getResultsInfo(birthDate, inz, lastName)
                    .statusCode(429);
        });
        step("Проверяем, что ответ содержит ошибку множественных запросов при проверки данных", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError())
                .as("Тип ошибки должен быть " + Errors.EXCEEDED_REQUESTS.getValue() + " при превышении лимита")
                .isEqualTo(Errors.EXCEEDED_REQUESTS.getValue());
            assertThat(errorResponse.getMessage())
                .as("Сообщение должно содержать: " + ErrorMessages.EXCEEDED_MESSAGE.getValue())
                .isEqualTo(ErrorMessages.EXCEEDED_MESSAGE.getValue());
            assertThat(errorResponse.getAttempts())
                .as("Количество попыток должно быть null при блокировке")
                .isNull();
        });
    }

    @ParameterizedTest
    @DisplayName("Получение результатов анализов с невалидными данными - проверка null параметров")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    @Story("Input Validation")
    @Description("Проверяем, что API корректно валидирует null значения для всех обязательных параметров")
    @Severity(SeverityLevel.NORMAL)
    void testNullParametersValidation(String nullParameter) {
        step("Получаем результаты анализов с null параметром '" + nullParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(nullParameter) ? null : fakerTestData.birthDate,
                    "inz".equals(nullParameter) ? null : fakerTestData.inz,
                    "lastName".equals(nullParameter) ? null : fakerTestData.lastName
            );
        });
        step("Статус код ответа \"400\"", () -> {
            response.statusCode(400);
        });
        step("Тело ответа содержит информацию об ошибке валидации", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError())
                .as("Тип ошибки должен быть " + Errors.BAD_REQUEST.getValue() + " для null параметров")
                .isEqualTo(Errors.BAD_REQUEST.getValue());
            assertThat(errorResponse.getMessage())
                .as("Сообщение должно содержать информацию о валидации: " + ErrorMessages.BAD_REQUEST_MESSAGE.getValue())
                .isEqualTo(ErrorMessages.BAD_REQUEST_MESSAGE.getValue());
        });
    }

    @ParameterizedTest
    @DisplayName("Получение результатов анализов с невалидными данными параметров")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    @Story("Data Format Validation")
    @Description("Проверяем, что API корректно обрабатывает невалидные форматы данных для всех параметров")
    @Tag("server-bug")
    @Severity(SeverityLevel.MINOR)
    void testInvalidDataFormats(String invalidParameter) {
        step("Получаем результаты анализов с невалидным параметром '" + invalidParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(invalidParameter) ? INVALID_BIRTHDAY.getValue() : fakerTestData.birthDate,
                    "inz".equals(invalidParameter) ? INVALID_INZ.getValue() : fakerTestData.inz,
                    "lastName".equals(invalidParameter) ? INVALID_LAST_NAME.getValue() : fakerTestData.lastName
            );
        });
        step("Проверяем тело ответа для параметра '" + invalidParameter + "'", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            if ("birthDate".equals(invalidParameter)) {
                response.statusCode(400);
                assertThat(errorResponse.getError())
                    .as("Для невалидной даты ошибка должна быть " + Errors.BAD_REQUEST.getValue())
                    .isEqualTo(Errors.BAD_REQUEST.getValue());
            } else if ("inz".equals(invalidParameter)) {
                response.statusCode(500);
                assertThat(errorResponse.getError())
                    .as("Для невалидного ИНЗ ошибка должна быть " + Errors.INTERNAL_ERROR.getValue())
                    .isEqualTo(Errors.INTERNAL_ERROR.getValue());
            } else if ("lastName".equals(invalidParameter)) {
                response.statusCode(409);
                assertThat(errorResponse.getError())
                    .as("Для невалидной фамилии ошибка должна быть " + Errors.CONFLICT.getValue())
                    .isEqualTo(Errors.CONFLICT.getValue());
                assertThat(errorResponse.getAttempts())
                    .as("Количество попыток должно быть равно лимиту для конфликта: " + MAX_LIMIT_ATTEMPTS.getValue())
                    .isEqualTo(MAX_LIMIT_ATTEMPTS.getValue());
            }
        });
    }

    @ParameterizedTest
    @DisplayName("Проверка безопасности API от безопасных SQL-инъекций")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    @Story("Security Testing")
    @Description("Проверяем, что API корректно блокирует попытки SQL-инъекций")
    @Severity(SeverityLevel.BLOCKER)
    void testUnauthResultsSecurity(String sqlParameter) {
        step("Проверяем безопасность параметра '" + sqlParameter + "' от SQL-инъекций", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(sqlParameter) ? fakerTestData.birthDate + "' OR '1'='1" : fakerTestData.birthDate,
                    "inz".equals(sqlParameter) ? fakerTestData.inz + "' UNION SELECT 1" : fakerTestData.inz,
                    "lastName".equals(sqlParameter) ? fakerTestData.lastName + "' AND '1'='1" : fakerTestData.lastName
            );
        });
        step("Проверяем, что API блокирует подозрительные запросы с кодом \"403\"", () -> {
            response.statusCode(403);
        });
    }

}
