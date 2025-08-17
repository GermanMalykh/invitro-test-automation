package tests.api.tests;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.TestData;
import tests.api.models.ResultsResponse;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class UnauthResultsApiTest extends ApiConfig {
    protected ValidatableResponse response;
    private final static InvitroApiClient apiClient = new InvitroApiClient();
    private final static TestData testData = new TestData();

    @Test
    @DisplayName("Получение результатов анализов по данным пациента, которого нет в системе")
    void testLabResultsNotFoundPatientData() {
        step("Получаем результаты анализов", () -> {
            response = apiClient.getResultsInfo(
                    testData.birthDate,
                    testData.inz,
                    testData.lastName);
        });
        step("Проверяем, что статус код ответа: \"409\"", () -> {
            response.statusCode(409);
        });
        step("Проверяем, что ответ содержит ошибку конфликта проверки данных", () -> {
            ResultsResponse resultsResponse = response.extract().as(ResultsResponse.class);
            assertThat(resultsResponse.getError()).isEqualTo("Conflict");
            assertThat(resultsResponse.getMessage()).isEqualTo("");
            assertThat(resultsResponse.getAttempts()).isEqualTo(4);
        });
    }

    @Test
    @DisplayName("Блокировка проверки результатов при частых запросах")
    void testLabResultsRateLimitExceeded() {
        String birthday = testData.birthDate;
        String inz = testData.inz;
        String lastName = testData.lastName;
        step("Получаем результаты анализов несколько раз подряд", () -> {
            for (int i = 0; i < 4; i++) {
                apiClient.getResultsInfo(birthday, inz, lastName);
            }
        });
        step("Получаем результаты анализов и проверяем статус код ответа \"429\"", () -> {
            response = apiClient.getResultsInfo(birthday, inz, lastName)
                    .statusCode(429);
        });
        step("Проверяем, что ответ содержит ошибку множественных запросов при проверки данных", () -> {
            ResultsResponse resultsResponse = response.extract().as(ResultsResponse.class);
            assertThat(resultsResponse.getError()).isEqualTo("Too Many Requests");
            assertThat(resultsResponse.getMessage()).isEqualTo("Вы израсходовали количество попыток, возвращайтесь позже");
            assertThat(resultsResponse.getAttempts()).isNull();
        });
    }

    @ParameterizedTest
    @DisplayName("Получение результатов анализов с невалидными данными - проверка null параметров")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    void testNullParametersValidation(String nullParameter) {
        step("Получаем результаты анализов с null параметром '" + nullParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(nullParameter) ? null : testData.birthDate,
                    "inz".equals(nullParameter) ? null : testData.inz,
                    "lastName".equals(nullParameter) ? null : testData.lastName
            );
        });
        step("Проверяем ошибку валидации", () -> {
            response.statusCode(400);
//            TODO: проверить тело ответа
        });
    }

    @ParameterizedTest
    @DisplayName("Получение результатов анализов с невалидными данными параметров")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    void testInvalidDataFormats(String invalidParameter) {
        step("Получаем результаты анализов с невалидным параметром '" + invalidParameter + "'", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(invalidParameter) ? "17-08-2025" : testData.birthDate,
                    "inz".equals(invalidParameter) ? "validationTest" : testData.inz,
                    "lastName".equals(invalidParameter) ? "Имя" : testData.lastName
            );
        });
        step("Проверяем тело ответа для параметра '" + invalidParameter + "'", () -> {
            ResultsResponse resultsResponse = response.extract().as(ResultsResponse.class);
            if ("birthDate".equals(invalidParameter)) {
                response.statusCode(400);
                assertThat(resultsResponse.getError()).isEqualTo("Bad Request");
            } else if ("inz".equals(invalidParameter)) {
                response.statusCode(500);
                assertThat(resultsResponse.getError()).isEqualTo("Internal Server Error");
            } else if ("lastName".equals(invalidParameter)) {
                response.statusCode(409);
                assertThat(resultsResponse.getError()).isEqualTo("Conflict");
                assertThat(resultsResponse.getAttempts()).isEqualTo(4);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("Проверка безопасности API от безопасных SQL-инъекций")
    @ValueSource(strings = {"birthDate", "inz", "lastName"})
    void testApiSecuritySqlInjection(String sqlParameter) {
        step("Проверяем безопасность параметра '" + sqlParameter + "' от SQL-инъекций", () -> {
            response = apiClient.getResultsInfo(
                    "birthDate".equals(sqlParameter) ? "2007' OR '1'='1" : testData.birthDate,
                    "inz".equals(sqlParameter) ? "007' UNION SELECT 1" : testData.inz,
                    "lastName".equals(sqlParameter) ? "Смит' AND '1'='1" : testData.lastName
            );
        });
        step("Проверяем, что API блокирует подозрительные запросы с кодом \"403\"", () -> {
            response.statusCode(403);
        });
    }

}
