package tests.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.ErrorMessages;
import tests.api.constants.Errors;
import tests.api.constants.LetterProducts;
import tests.api.constants.NumericProducts;
import tests.api.constants.ProductType;
import tests.api.models.ErrorResponse;
import tests.api.models.ProductsInfoResponse;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Testing")
@Feature("Products Info API")
@Owner("germanmalykh")
@DisplayName("API тестирование: Получение информации о продуктах")
public class ProductsInfoApiTest extends ApiConfig {

    protected ValidatableResponse response;
    private final static InvitroApiClient apiClient = new InvitroApiClient();

    @Test
    @DisplayName("Получение информации о продукте с цифровым артикулом (Глюкоза)")
    @Story("Product Information")
    @Description("Проверяем, что API корректно возвращает информацию о продукте с цифровым артикулом 16 (Глюкоза)")
    @Severity(SeverityLevel.NORMAL)
    void testProductInfoCheckByNumericArticle() {
        step("Получаем информацию о продукте с цифровым артикулом " + NumericProducts.GLUCOSE.getValue(), () -> {
            response = apiClient.getProductsInfo(
                    NumericProducts.GLUCOSE.getValue(), null);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что тип продукта равен " + ProductType.COMMON_PRODUCT, () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getType())
                    .as("Тип продукта должен быть равен " + ProductType.COMMON_PRODUCT)
                    .isEqualTo(ProductType.COMMON_PRODUCT.toString());
        });
    }

    @Test
    @DisplayName("Получение информации о продукте с буквенным артикулом (Взятие крови)")
    @Story("Product Information")
    @Description("Проверяем, что API корректно возвращает информацию о продукте с буквенным артикулом VEN (Взятие крови)")
    @Severity(SeverityLevel.NORMAL)
    void testProductInfoCheckByLetterArticle() {
        step("Получаем информацию о продукте с буквенным артикулом " + LetterProducts.VEN.getValue(), () -> {
            response = apiClient.getProductsInfo(
                    LetterProducts.VEN.getValue(), null);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что тип продукта равен " + ProductType.SERVICE, () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getType())
                    .as("Тип продукта должен быть равен " + ProductType.SERVICE)
                    .isEqualTo(ProductType.SERVICE.toString());
        });
    }

    @Test
    @DisplayName("Получение информации о продукте без указания артикула")
    @Story("Input Validation")
    @Description("Проверяем, что API корректно обрабатывает запрос без указания артикула (articles=null)")
    @Severity(SeverityLevel.CRITICAL)
    void testProductInfoWithNullArticle() {
        step("Получаем информацию о продукте без указания артикула", () -> {
            response = apiClient.getProductsInfo(
                    null, null);
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для отсутствующего артикула", () -> {
           ErrorResponse errorResponse= response.extract().as(ErrorResponse.class);
           assertThat(errorResponse.getError()).as("Для ошибки валидации должен быть "
                           + Errors.BAD_REQUEST.getValue()).isEqualTo(Errors.BAD_REQUEST.getValue());
            assertThat(errorResponse.getMessage())
                    .as("Сообщение об ошибке должно быть: " + ErrorMessages.ARTICLE_MESSAGE.getValue())
                    .isEqualTo(ErrorMessages.ARTICLE_MESSAGE.getValue());
        });
    }

    @Test
    @DisplayName("Проверка валидации невалидного значения cityId")
    @Story("Input Validation")
    @Description("Проверяем, что API корректно валидирует невалидное значение для cityId")
    @Severity(SeverityLevel.MINOR)
    void testProductInfoInvalidValues() {
        step("Тестируем валидацию с невалидным cityId: not-a-guid", () -> {
            response = apiClient.getProductsInfo(NumericProducts.GLUCOSE.getValue(), "not-a-guid");
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
    }

    @ParameterizedTest
    @DisplayName("Проверка безопасности API от SQL-инъекций")
    @Story("Security Testing")
    @Description("Проверяем, что API корректно блокирует попытки SQL-инъекций в article и cityId")
    @ValueSource(strings = {"article", "cityId"})
    @Severity(SeverityLevel.BLOCKER)
    void testProductInfoSecurity(String sqlParameter) {
        step("Тестируем безопасность параметра '" + sqlParameter + "' от SQL-инъекций", () -> {
            response = apiClient.getProductsInfo(
                    "article".equals(sqlParameter) ? NumericProducts.GLUCOSE.getValue() + "' OR '1'='1" : NumericProducts.GLUCOSE.getValue(),
                    "cityId".equals(sqlParameter) ? "4d3ffba2-0ae9-427d-96e6-fed33cbee7b0" + "' UNION SELECT 1" : "4d3ffba2-0ae9-427d-96e6-fed33cbee7b0"
            );
        });
        step("Проверяем, что API блокирует подозрительные запросы с кодом \"403\"", () -> {
            response.statusCode(403);
        });
    }

    @Test
    @DisplayName("Получение информации о продукте с указанием cityId")
    @Story("Product Information")
    @Description("Проверяем, что API корректно возвращает информацию о продукте при указании cityId")
    @Severity(SeverityLevel.NORMAL)
    void testProductInfoByCity() {
        step("Получаем информацию о продукте с указанием cityId", () -> {
            response = apiClient.getProductsInfo(
                    LetterProducts.VEN.getValue(), "4d3ffba2-0ae9-427d-96e6-fed33cbee7b0");
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что API возвращает информацию о продукте с ценой", () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getPrice())
                    .as("Проверяем, что при передаче идентификатора города " +
                            "в информации о продукте указана цена")
                    .isNotNull();
        });
    }
}
