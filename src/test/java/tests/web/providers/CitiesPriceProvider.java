package tests.web.providers;

import org.junit.jupiter.params.provider.Arguments;
import tests.web.constants.FilePathConstants;
import tests.web.helpers.JsonConverter;
import tests.web.models.ProductsInfo;

import java.util.Arrays;
import java.util.stream.Stream;

public class CitiesPriceProvider {

    public static Stream<Arguments> provideCitiesData() {
        ProductsInfo productInfo = JsonConverter.deserialize(FilePathConstants.PRICE_BY_CITY_JSON, ProductsInfo.class);

        return Arrays.stream(productInfo.getCitiesInfo())
                .map(city -> Arguments.of(city, productInfo));
    }
}
