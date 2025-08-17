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

    @Description("GET /site/api/unauth/results - Получение результатов анализов")
    public ValidatableResponse getResultsInfo(String birthDate,
                                              String inz,
                                              String lastName) {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
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
}
