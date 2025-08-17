package tests.api.tests;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.specs.RestSpec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Disabled
public class DraftApiTest extends ApiConfig {

    private static final String USER_AGENT = "Mozilla/999.999 (Macintosh; Intel Mac OS X 99_99_99) Chrome/999.999.999.999 Safari/999.999";

    protected ValidatableResponse response;
    InvitroApiClient apiClient = new InvitroApiClient();

    @Test
    @DisplayName("Получение результатов анализов по данным пациента, которого нет в системе")
    void getLabResultsByNotFoundPatientData() {
        step("Получение результатов анализов по данным пациента, которого нет в системе", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("birthDate", "1990-01-01")
                    .queryParam("inz", "123456789")
                    .queryParam("lastName", "Иванов")
                    .when()
                    .get("/site/api/unauth/results")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(is(409))
                    .contentType(ContentType.JSON);
        });
    }

    @Test
    @DisplayName("Получение результатов анализов по данным пациента, которого нет в системе")
    void getLabResultsByNotFoundPatientData2() {
        step("Получение результатов анализов по данным пациента, которого нет в системе", () -> {
            response = apiClient.getResultsInfo("1990-01-01","123456789","Иванов");
        });
        step("Ответ содержит статус код: \"409\"", () -> {
            response.statusCode(409);
        });

    }









    @Test
    @DisplayName("Тест блокировки при частых запросах")
    void testRequestBlockingOnFrequentCalls() {
        step("Отправка множественных запросов для проверки блокировки", () -> {
            // Отправляем несколько запросов подряд
            for (int i = 0; i < 4; i++) {
                given(RestSpec.requestSpecification)
                        .header("User-Agent", USER_AGENT)
                        .queryParam("birthDate", "1990-01-01")
                        .queryParam("inz", "123456789")
                        .queryParam("lastName", "Сидоров3")
                        .when()
                        .get("/site/api/unauth/results");
            }

            // Проверяем, что последний запрос заблокирован
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("birthDate", "1990-01-01")
                    .queryParam("inz", "123456789")
                    .queryParam("lastName", "Сидоров3")
                    .when()
                    .get("/site/api/unauth/results")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(is(429)) // Коды блокировки
                    .contentType(ContentType.JSON);
        });
    }





    @Test
    @DisplayName("Поиск ближайших офисов для сдачи анализов")
    void testFindNearestLabOffices() {
        step("Поиск ближайших офисов для сдачи анализов", () -> {
            // Получаем список городов
            String cityId = step("Получение ID первого города из списка", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .when()
                            .get("site/api/cities")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .extract()
                            .path("[0].id")
            );

            // Ищем офисы в выбранном городе
            step("Поиск офисов в выбранном городе", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .queryParam("cityId", cityId)
                            .when()
                            .get("site/api/cart/offices")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .statusCode(200)
                            .body("$", hasSize(greaterThan(0)))
                            .body("[0].office.id", notNullValue())
                            .body("[0].office.name", notNullValue())
                            .body("[0].office.address", notNullValue())
            );
        });
    }





    @Test
    @DisplayName("Поиск ближайших офисов для неизвестного ID города")
    void findUnknownLabOffices() {
        step("Поиск ближайших офисов для сдачи анализов", () -> {

                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .queryParam("cityId", "15f70e32-6e45-ee11-80ea-00155d6f9367")
                            .when()
                            .get("site/api/cart/offices")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .statusCode(500);

        });
    }





    @Test
    @DisplayName("Поиск анализов по артикулу для записи")
    void testSearchTestsByArticleForBooking() {
        step("Поиск анализов по артикулу для записи", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("articles", "16")
                    .when()
                    .get("site/api/products")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(200)
                    .body("$", hasSize(greaterThan(0)))
                    .body("[0].id", notNullValue())
                    .body("[0].article", equalTo("16"))
                    .body("[0].name", notNullValue());
        });
    }

    @Test
    @DisplayName("Поиск анализов по артикулу для записи")
    void testSearchTestsWithoutArticleForBooking() {
        step("Поиск анализов по артикулу для записи", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .when()
                    .get("site/api/products")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(400);
        });
    }






    @Test
    @DisplayName("Расчет стоимости комплекса анализов")
    void testCalculateTestPackagePrice() {
        step("Расчет стоимости комплекса анализов", () -> {
            // Получаем ID офиса
            String officeId = step("Получение ID первого офиса", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .queryParam("cityId","11901329-6550-11eb-854f-00155d0f0506")
                            .when()
                            .get("site/api/cart/offices")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .extract()
                            .path("[0].id")
            );

            // Получаем ID теста
            String testId = step("Получение ID теста по артикулу", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .queryParam("articles", "16")
                            .when()
                            .get("site/api/products")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .extract()
                            .path("[0].id")
            );

            // Рассчитываем стоимость
            step("Расчет стоимости анализов", () -> {
                String requestBody = String.format("{\n" +
                        "    \"orderProductsKeys\": [\n" +
                        "        {\n" +
                        "            \"biomaterialId\": \"%s\",\n" +
                        "            \"productId\": \"%s\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"territory\": \"MSC\"\n" +
                        "}", testId, testId);

                given(RestSpec.requestSpecification)
                        .header("User-Agent", USER_AGENT)
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("site/api/cart/offices/" + officeId + "/calculate")
                        .then()
                        .spec(RestSpec.responseSpecification)
                        .statusCode(200)
                        .body("totalPrice", notNullValue());
            });
        });
    }

    @Test
    @DisplayName("Поиск анализов по названию для пациента")
    void testSearchTestsByNameForPatient() {
        step("Поиск анализов по названию для пациента", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("articles", "ОБЩ278")
                    .when()
                    .get("site/api/products")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(200)
                    .body("$", hasSize(greaterThan(0)))
                    .body("[0].name", containsStringIgnoringCase("общий"));
        });
    }

    @Test
    @DisplayName("Проверка доступности офиса для записи")
    void testCheckOfficeAvailabilityForBooking() {
        step("Проверка доступности офиса для записи", () -> {
            // Получаем список офисов
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .when()
                    .get("site/api/cart/offices")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(200)
                    .body("$", hasSize(greaterThan(0)))
                    .body("[0].isActive", notNullValue());
        });
    }

    @Test
    @DisplayName("Получение информации о городе для выбора офиса")
    void testGetCityInfoForOfficeSelection() {
        step("Получение информации о городе для выбора офиса", () -> {
            // Получаем список городов
            String cityId = step("Получение ID первого города", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .when()
                            .get("site/api/cities")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .extract()
                            .path("[0].id")
            );

            // Получаем детальную информацию о городе
            step("Получение детальной информации о городе", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .when()
                            .get("site/api/cities/" + cityId)
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .statusCode(200)
                            .body("id", equalTo(cityId))
                            .body("name", notNullValue())
                            .body("region", notNullValue())
            );
        });
    }

    @Test
    @DisplayName("Поиск анализов по нескольким артикулам для комплексного обследования")
    void testSearchMultipleTestsForComprehensiveExam() {
        step("Поиск анализов по нескольким артикулам для комплексного обследования", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("articles", "16,ОБЩ278,БИОХ")
                    .when()
                    .get("site/api/products")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(200)
                    .body("$", hasSize(greaterThan(0)));
        });
    }


    @Test
    @DisplayName("Валидация некорректных данных пациента")
    void testInvalidPatientDataValidation() {
        step("Валидация некорректных данных пациента", () -> {
            given(RestSpec.requestSpecification)
                    .header("User-Agent", USER_AGENT)
                    .queryParam("birthDate", "invalid-date")
                    .queryParam("inz", "abc")
                    .queryParam("lastName", "")
                    .when()
                    .get("/site/api/unauth/results")
                    .then()
                    .spec(RestSpec.responseSpecification)
                    .statusCode(anyOf(is(400), is(422), is(500)));
        });
    }

    @Test
    @DisplayName("Проверка корректности формата ответов API")
    void testApiResponseFormat() {
        step("Проверка корректности формата ответов API", () -> {
            // Проверяем формат ответа для городов
            step("Проверка формата ответа для городов", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .when()
                            .get("site/api/cities")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("$", hasSize(greaterThan(0)))
                            .body("[0]", hasKey("id"))
                            .body("[0]", hasKey("name"))
            );

            // Проверяем формат ответа для офисов
            step("Проверка формата ответа для офисов", () ->
                    given(RestSpec.requestSpecification)
                            .header("User-Agent", USER_AGENT)
                            .when()
                            .get("site/api/cart/offices")
                            .then()
                            .spec(RestSpec.responseSpecification)
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("$", hasSize(greaterThan(0)))
                            .body("[0]", hasKey("id"))
                            .body("[0]", hasKey("name"))
            );
        });
    }
}
