package tests.android.providers;

import org.junit.jupiter.params.provider.Arguments;
import constants.FilePathConstants;
import helpers.JsonConverter;
import tests.android.models.CartData;

import java.util.Arrays;
import java.util.stream.Stream;

public class CartDataProvider {

    public static Stream<Arguments> provideCartData() {
        CartData cartData = JsonConverter.deserialize(
                FilePathConstants.CART_PRODUCT_JSON_ANDROID, 
                CartData.class
        );

        return Arrays.stream(cartData.getProductsInfo())
                .map(product -> Arguments.of(product));
    }
}
