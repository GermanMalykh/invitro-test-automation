package tests.android.helpers;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import tests.android.config.ConfigReader;
import specs.RestSpec;

import static java.lang.String.format;

public class BrowserstackGetter {

    public static String videoUrl(String sessionId) {
        return getSessionInfo(sessionId)
                .path("automation_session.video_url");
    }

    public static String fullInfoPublicUrl(String sessionId) {
        return getSessionInfo(sessionId)
                .path("automation_session.public_url");
    }

    public static ExtractableResponse<Response> getSessionInfo(String sessionId) {
        String url = format("https://api.browserstack.com/app-automate/sessions/%s.json", sessionId);

        return RestAssured.given(RestSpec.requestSpecification)
                .auth().basic(
                        ConfigReader.get("browserstack.user"),
                        ConfigReader.get("browserstack.key"))
                .when()
                .get(url)
                .then()
                .spec(RestSpec.responseSpecification)
                .statusCode(200)
                .extract();
    }
}
