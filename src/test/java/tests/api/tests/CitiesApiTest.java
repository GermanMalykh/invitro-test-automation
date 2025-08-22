package tests.api.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.Errors;
import tests.api.constants.FakerTestData;
import tests.api.constants.TestData;
import tests.api.models.CityResponse;
import tests.api.models.ErrorResponse;

import java.util.Arrays;

import static io.qameta.allure.Allure.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("api")
@Owner("germanmalykh")
@DisplayName("[API] Cities API Tests")
public class CitiesApiTest extends ApiConfig {

    protected ValidatableResponse response;

    private final InvitroApiClient apiClient = new InvitroApiClient();

    private final FakerTestData fakerTestData = new FakerTestData();

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("[API] Получение списка всех городов")
    @Description("Тест проверяет успешное получение списка всех городов с валидацией структуры ответа")
    void testGetAllCities() {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities();
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что ответ заполнен согласно структуре", () -> {
            CityResponse[] cityResponse = response.extract().as(CityResponse[].class);
            assertThat(cityResponse[0].getId()).isNotNull();
            assertThat(cityResponse[0].getName()).isNotNull();
            assertThat(cityResponse[0].getRegion()).isNotNull();
            assertThat(cityResponse[0].getTerritory()).isNotNull();
            assertThat(cityResponse[0].isCapital()).isNotNull();
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Получение информации о конкретном городе")
    @Description("Тест проверяет получение детальной информации о конкретном городе по его ID")
    void testGetCityInfo() {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities().statusCode(200);
        });
        step("Получаем информацию о городе", () -> {
            CityResponse[] cityResponse = response.extract().as(CityResponse[].class);
            String cityId = cityResponse[0].getId();
            response = apiClient.getCityInfo(cityId);
            addAttachment("ID города", "text/plain", cityId);
            addAttachment("Название города", "text/plain", cityResponse[0].getName());
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что ответ заполнен согласно структуре", () -> {
            CityResponse cityResponse = response.extract().as(CityResponse.class);
            assertThat(cityResponse.isHomeVisitAvailable()).isNotNull();
            assertThat(cityResponse.isOnlineRegistrationSupported()).isNotNull();
        });
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Получение информации о городе с невалидным идентификатором")
    @Description("Тест проверяет корректную обработку невалидного формата ID города")
    void testGetCityInfoWithInvalidId() {
        step("Выполняем запрос на получение информации о городе с невалидным идентификатором города", () -> {
            response = apiClient.getCityInfo(TestData.INVALID_GUID.getValue());
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для неправильного формата идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.BAD_REQUEST.getValue());
        });
    }

    @Test
    @Tag("server-bug")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Получение информации о городе со случайным идентификатором")
    @Description("Тест проверяет обработку случайного GUID при запросе информации о городе")
    void testGetCityInfoWithRandomId() {
        step("Выполняем запрос на получение информации о городе со случайным идентификатором", () -> {
            addAttachment("Случайный GUID", "text/plain", fakerTestData.guid);
            response = apiClient.getCityInfo(fakerTestData.guid);
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

    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Проверка соответствия \"Страна\" - \"Столица\". ")
    @ParameterizedTest(name = "Стране \"{1}\" соответствует столица \"{0}\"")
    @MethodSource("tests.api.providers.CapitalsProvider#provideCapitalCountryPairs")
    void testCapitalCityCountryCorrespondence(String cityName, String expectedCountry) {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities().statusCode(200);
        });
        step("Проверяем, что для города '" + cityName + "' отображается актуальная информация о признаке столицы и страны", () -> {
            CityResponse[] cities = response.extract().as(CityResponse[].class);
            CityResponse foundCity = Arrays.stream(cities)
                    .filter(city -> cityName.equals(city.getName()))
                    .findFirst()
                    .orElse(null);

            assertThat(foundCity.getName()).isEqualTo(cityName);
            assertThat(foundCity.isCapital()).isTrue();
            assertThat(foundCity.getTerritory()).isEqualTo(expectedCountry);

            addAttachment("Информация о городе", "application/json",
                    String.format("{\"name\": \"%s\", \"territory\": \"%s\", \"capital\": %s}",
                            foundCity.getName(), foundCity.getTerritory(), foundCity.isCapital()));
        });
    }
}
