package tests.api.client;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import tests.api.constants.Endpoints;
import tests.api.constants.ApiConfigConstants;
import tests.api.specs.RestSpec;

import java.util.HashMap;
import java.util.Map;

public class InvitroApiClient {

    private static final String USER_AGENT = ApiConfigConstants.USER_AGENT.getValue();
    private static final String USER_AGENT_MOBILE = ApiConfigConstants.USER_AGENT_MOBILE.getValue();

    /**
     * Установка российских заголовков для обхода геоблокировки
     */
    private Map<String, String> russianHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("DNT", "1");
        headers.put("Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("Sec-Fetch-Dest", "document");
        headers.put("Sec-Fetch-Mode", "navigate");
        headers.put("Sec-Fetch-Site", "none");
        headers.put("Cache-Control", "max-age=0");
        return headers;
    }

    @Description("GET /site/api/unauth/results - Получение результатов анализов")
    public ValidatableResponse getResultsInfo(String birthDate,
                                              String inz,
                                              String lastName) {
        Map<String, String> headers = russianHeaders();
        Map<String, String> params = new HashMap<>();
        params.put("birthDate", birthDate);
        params.put("inz", inz);
        params.put("lastName", lastName);
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .queryParams(params)
                .get(Endpoints.RESULTS_PATH)
                .then()
                .spec(RestSpec.responseSpecification);
    }

    @Description("GET /v3/unauth/results - Получение результатов анализов (Mobile)")
    public ValidatableResponse getResultsInfoMobile(String birthDate,
                                                    String inz,
                                                    String lastName) {
        RestAssured.baseURI = ApiConfigConstants.BASE_URL_MOBILE.getValue();
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT_MOBILE);
        headers.put("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("DNT", "1");
        headers.put("Connection", "keep-alive");
        Map<String, String> params = new HashMap<>();
        params.put("birthDate", birthDate);
        params.put("inz", inz);
        params.put("lastName", lastName);
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .queryParams(params)
                .get(Endpoints.RESULTS_PATH_MOBILE)
                .then()
                .spec(RestSpec.responseSpecification);
    }

    @Description("GET /site/api/products - Получение информации о позиции")
    public ValidatableResponse getProductsInfo(String articles,
                                               String cityId) {
        Map<String, String> headers = russianHeaders();
        Map<String, String> params = new HashMap<>();
        params.put("cityId", cityId);
        if (articles != null) params.put("articles", articles);
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .queryParams(params)
                .get(Endpoints.PRODUCTS_PATH)
                .then()
                .spec(RestSpec.responseSpecification);
    }

    @Description("GET /site/api/cities - Получение списка всех городов")
    public ValidatableResponse getAllCities() {
        Map<String, String> headers = russianHeaders();
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .get(Endpoints.ALL_CITIES_PATH)
                .then()
                .spec(RestSpec.responseSpecification);
    }

    @Description("GET /site/api/cities/cityId - Получение информации о городе")
    public ValidatableResponse getCityInfo(String cityId) {
        Map<String, String> headers = russianHeaders();
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .get(String.format(Endpoints.SINGLE_CITIES_PATH, cityId))
                .then()
                .spec(RestSpec.responseSpecification);
    }

    @Description("GET /site/api/cart/offices - Получение об офисе")
    public ValidatableResponse getOfficeInfo(String cityId) {
        Map<String, String> headers = russianHeaders();
        Map<String, String> params = new HashMap<>();
        params.put("cityId", cityId);
        return RestAssured.given(RestSpec.requestSpecification)
                .contentType(ContentType.JSON)
                .headers(headers)
                .queryParams(params)
                .get(Endpoints.OFFICES_PATH)
                .then()
                .spec(RestSpec.responseSpecification);
    }
}
