package tests.api.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import utils.AllureEnv;
import tests.api.constants.ApiConfigConstants;

public class ApiConfig {

    @BeforeAll
    public static void setUp() {
        String testType = System.getProperty("test.type");
        Assumptions.assumeTrue("api".equals(testType) || "all".equals(testType) || testType == null,
                "Этот тест должен запускаться только для API тестов или всех тестов");

        RestAssured.baseURI = ApiConfigConstants.BASE_URL.getValue();

        setupRussianHeaders();
    }

    private static void setupRussianHeaders() {
        RestAssured.given()
                .header("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Cache-Control", "max-age=0");
    }

    @AfterAll
    static void afterAll() {
        AllureEnv.writeAllureEnvironment();
    }
}
