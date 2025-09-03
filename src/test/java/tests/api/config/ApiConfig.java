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
        // Проверяем, что это действительно API тест
        String testType = System.getProperty("test.type");
        Assumptions.assumeTrue("api".equals(testType) || "all".equals(testType) || testType == null, 
            "Этот тест должен запускаться только для API тестов или всех тестов");
        
        RestAssured.baseURI = ApiConfigConstants.BASE_URL.getValue();
    }

    @AfterAll
    static void afterAll() {
        AllureEnv.writeAllureEnvironment();
    }
}
