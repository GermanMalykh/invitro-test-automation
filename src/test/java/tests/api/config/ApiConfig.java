package tests.api.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import tests.api.utils.AllureEnv;
import tests.api.constants.ApiConfigConstants;

public class ApiConfig {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = ApiConfigConstants.BASE_URL.getValue();
    }

    @AfterAll
    static void afterAll() {
        AllureEnv.writeAllureEnvironment();
    }
}
