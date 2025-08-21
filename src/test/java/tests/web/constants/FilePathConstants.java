package tests.web.constants;

import java.io.File;

public class FilePathConstants {
    public static final String TEST_RESOURCES = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    public static final String JSON_PATH = TEST_RESOURCES + "jsons" + File.separator;
    public static final String PRICE_BY_CITY_JSON = JSON_PATH + "priceByCity.json";
    public static final String CART_PRODUCT_JSON = JSON_PATH + "cartJson.json";
}
