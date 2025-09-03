package tests.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.api.client.InvitroApiClient;
import tests.api.config.ApiConfig;
import tests.api.constants.Errors;
import tests.api.constants.LetterProducts;
import tests.api.constants.NumericProducts;
import tests.api.constants.ProductType;
import tests.api.constants.TestData;
import tests.api.models.CityResponse;
import tests.api.models.ErrorResponse;
import tests.api.models.ProductsInfoResponse;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Owner("germanmalykh")
@DisplayName("API Tests")
public class ProductsInfoApiTest extends ApiConfig {

    protected ValidatableResponse response;

    private String cityId;

    private final InvitroApiClient apiClient = new InvitroApiClient();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Получение информации о продукте с цифровым артикулом")
    @Description("Проверяем, что API корректно возвращает информацию о продукте с цифровым артикулом 16 (Глюкоза)")
    void getProductInfoByNumericArticle() {
        step("Получаем информацию о продукте с цифровым артикулом " + NumericProducts.GLUCOSE.getValue(), () -> {
            response = apiClient.getProductsInfo(
                    NumericProducts.GLUCOSE.getValue(),
                    null);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что ответ заполнен согласно структуре", () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getType())
                    .isEqualTo(ProductType.COMMON_PRODUCT.toString());
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Получение информации о продукте с буквенным артикулом")
    @Description("Проверяем, что API корректно возвращает информацию о продукте с буквенным артикулом VEN (Взятие крови)")
    void getProductInfoByLetterArticle() {
        step("Получаем информацию о продукте с буквенным артикулом", () -> {
            response = apiClient.getProductsInfo(
                    LetterProducts.VEN.getValue(),
                    null);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что ответ заполнен согласно структуре", () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getType())
                    .isEqualTo(ProductType.SERVICE.toString());
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Получение информации о продукте с указанием существующего идентификатора города")
    @Description("Проверяем, что API корректно возвращает информацию о продукте при указании cityId")
    void getProductInfoByCityId() {
        step("Выполняем запрос на получение списка всех городов", () -> {
            response = apiClient.getAllCities().statusCode(200);
        });
        step("Получаем идентификатор города", () -> {
            CityResponse[] cityResponse = response.extract().as(CityResponse[].class);
            cityId = cityResponse[0].getId();
        });
        step("Получаем информацию о продукте с указанием идентификатора города", () -> {
            response = apiClient.getProductsInfo(
                    LetterProducts.VEN.getValue(),
                    cityId);
        });
        step("Проверяем, что статус код ответа: \"200\"", () -> {
            response.statusCode(200);
        });
        step("Проверяем, что API возвращает информацию о продукте с ценой для указанного города", () -> {
            ProductsInfoResponse[] productsInfoResponse = response.extract().as(ProductsInfoResponse[].class);
            assertThat(productsInfoResponse).isNotEmpty();
            assertThat(productsInfoResponse[0]).isNotNull();
            assertThat(productsInfoResponse[0].getPrice()).isNotNull();
        });
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Получение информации о продукте с пустым идентификатором артикула")
    @Description("Проверяем, что API корректно обрабатывает запрос без указания артикула (articles=null)")
    void getProductInfoWithNullArticle() {
        step("Получаем информацию о продукте без указания артикула", () -> {
            response = apiClient.getProductsInfo(
                    null,
                    null);
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для отсутствующего артикула", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            assertThat(errorResponse.getError()).isEqualTo(Errors.BAD_REQUEST.getValue());
        });
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Получение информации о продукте с невалидным форматом идентификатора города")
    @Description("Проверяем, что API корректно валидирует невалидное значение для cityId")
    void getProductInfoWithInvalidValues() {
        step("Выполняем запрос на получение информации о продукте с невалидным идентификатором города", () -> {
            response = apiClient.getProductsInfo(
                    NumericProducts.GLUCOSE.getValue(),
                    TestData.INVALID_GUID.getValue());
        });
        step("Проверяем, что статус код ответа: \"400\"", () -> {
            response.statusCode(400);
        });
        step("Проверяем, что API возвращает ошибку валидации для неправильного формата идентификатора города", () -> {
            ErrorResponse errorResponse = response.extract().as(ErrorResponse.class);
            Assertions.assertThat(errorResponse.getError()).isEqualTo(Errors.BAD_REQUEST.getValue());
        });
    }
}
