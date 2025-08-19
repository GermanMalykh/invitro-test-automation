package tests.api.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.Errors;
import tests.api.constants.FakerTestData;
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
@DisplayName("Office API Tests")
public class OfficeApiTest extends ApiConfig {

    protected ValidatableResponse response;

    private final String[] cityNameHolder = new String[1];
    private final String[] cityIdHolder = new String[1];

    private final InvitroApiClient apiClient = new InvitroApiClient();
    private final FakerTestData fakerTestData = new FakerTestData();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Поиск офиса с существующим идентификатору города")
    @Description("Тест успешного поиска офиса по корректному ID города")
    void testFindOfficeByCityId() {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities().statusCode(200);
        });
        step("Получаем информацию о городе", () -> {
            CityResponse[] cityResponses = response.extract().as(CityResponse[].class);
            cityIdHolder[0] = cityResponses[0].getId();
            cityNameHolder[0] = cityResponses[0].getName();
        });
        step("Получаем информацию об офисе в городе", () -> {
            response = apiClient.getOfficeInfo(cityIdHolder[0]);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем что информация об офисе отображается для выбранного города", () -> {
            OfficeResponse[] officeResponses = response.extract().as(OfficeResponse[].class);
            assertThat(officeResponses[0].getOffice()).isNotNull();
            assertThat(officeResponses[0].getOffice().getAddress()).contains(cityNameHolder[0]);
        });
    }

    @Test
    @Tag("server-bug")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Поиск офиса со случайным идентификатором города")
    @Description("Тест обработки случайного GUID при поиске офиса")
    void testFindOfficeByRandomId() {
        step("Выполняем запрос на получение информации об офисе со случайным идентификатором города", () -> {
            addAttachment("Случайный GUID", "text/plain", fakerTestData.guid);
            response = apiClient.getOfficeInfo(fakerTestData.guid);
        });
        step("Проверяем, что статус код ответа: \"500\"", () -> {
            response.statusCode(500);
        });
        step("Проверяем, что API возвращает ошибку валидации для случайного идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.INTERNAL_ERROR.getValue());
            Assertions.assertThat(errorResponse.getMessage()).isNull();
        });
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("[API] Поиск офиса с пустым идентификатором города")
    @Description("Тест валидации при передаче null ID города")
    void testFindOfficeWithNullId() {
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
    void testFindOfficeWithInvalidId() {
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
