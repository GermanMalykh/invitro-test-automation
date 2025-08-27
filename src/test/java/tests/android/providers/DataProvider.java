package tests.android.providers;

import org.junit.jupiter.params.provider.Arguments;
import constants.FilePathConstants;
import helpers.JsonConverter;
import tests.android.models.CartData;
import tests.android.models.CitySectionsData;

import java.util.Arrays;
import java.util.stream.Stream;

public class DataProvider {

    public static Stream<Arguments> provideCartData() {
        CartData cartData = JsonConverter.deserialize(
                FilePathConstants.CART_PRODUCT_JSON_ANDROID,
                CartData.class
        );

        return Arrays.stream(cartData.getProductsInfo())
                .map(Arguments::of);
    }


    public static Stream<Arguments> provideCitySectionsData() {
        CitySectionsData city = JsonConverter.deserialize(
                FilePathConstants.CITY_SECTIONS_JSON,
                CitySectionsData.class
        );

        return Arrays.stream(city.getCityInfo())
                .map(Arguments::of);
    }
}
