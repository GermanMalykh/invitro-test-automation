package tests.api.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.Errors;
import constants.FakerConstants;
import tests.api.constants.TestData;
import tests.api.models.CityResponse;
import tests.api.models.ErrorResponse;
import tests.api.models.OfficeResponse;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;

@Tag("api")
@Owner("germanmalykh")
@DisplayName("API Tests")
public class OfficeApiTest extends ApiConfig {

    protected ValidatableResponse response;

    private String cityId;
    private String cityName;

    private final InvitroApiClient apiClient = new InvitroApiClient();
    private final FakerConstants fakerConstants = new FakerConstants();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Поиск офиса с существующим идентификатору города")
    @Description("Тест успешного поиска офиса по корректному ID города")
    void findOfficeByCityId() {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities().statusCode(200);
        });
        step("Получаем информацию о городе", () -> {
            CityResponse[] cityResponses = response.extract().as(CityResponse[].class);
            cityId = cityResponses[0].getId();
            cityName = cityResponses[0].getName();
        });
        step("Получаем информацию об офисе в городе", () -> {
            response = apiClient.getOfficeInfo(cityId);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем что информация об офисе отображается для выбранного города", () -> {
            OfficeResponse[] officeResponses = response.extract().as(OfficeResponse[].class);
            assertThat(officeResponses[0].getOffice()).isNotNull();
            assertThat(officeResponses[0].getOffice().getAddress()).contains(cityName);
        });
    }

    @Test
    @Tag("server-bug")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Поиск офиса со случайным идентификатором города")
    @Description("Тест обработки случайного GUID при поиске офиса")
    void findOfficeByRandomId() {
        step("Выполняем запрос на получение информации об офисе со случайным идентификатором города", () -> {
            addAttachment("Случайный GUID", "text/plain", fakerConstants.guid);
            response = apiClient.getOfficeInfo(fakerConstants.guid);
        });
        step("Проверяем, что статус код ответа: \"500\"", () -> {
            response.statusCode(500);
        });
        step("Проверяем, что API возвращает ошибку валидации для случайного идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.INTERNAL_ERROR.getValue());
        });
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("[API] Поиск офиса с пустым идентификатором города")
    @Description("Тест валидации при передаче null ID города")
    void findOfficeWithNullId() {
        step("Выполняем запрос на получение информации об офисе без передачи идентификатора города", () -> {
            response = apiClient.getOfficeInfo(null);
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для отсутствующего идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.BAD_REQUEST.getValue());
        });
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("[API] Поиск офиса с невалидным форматом идентификатора города")
    @Description("Тест валидации при передаче невалидного формата ID города")
    void findOfficeWithInvalidId() {
        step("Выполняем запрос на получение информации об офисе с невалидным идентификатором города", () -> {
            response = apiClient.getOfficeInfo(TestData.INVALID_GUID.getValue());
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для невалидного формата идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.BAD_REQUEST.getValue());
        });
    }

}
