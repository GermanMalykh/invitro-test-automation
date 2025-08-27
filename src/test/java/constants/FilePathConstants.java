package constants;

import java.io.File;

public class FilePathConstants {

    public static final String TEST_RESOURCES = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

    //WEB
    public static final String JSON_PATH_WEB = TEST_RESOURCES + "jsons" + File.separator + "web" + File.separator;
    public static final String PRICE_BY_CITY_JSON = JSON_PATH_WEB + "priceByCity.json";
    public static final String CART_PRODUCT_JSON_WEB = JSON_PATH_WEB + "cartJson.json";

    //ANDROID
    public static final String JSON_PATH_ANDROID = TEST_RESOURCES + "jsons" + File.separator + "android" + File.separator;
    public static final String CART_PRODUCT_JSON_ANDROID = JSON_PATH_ANDROID + "cartJson.json";
    public static final String CITY_SECTIONS_JSON = JSON_PATH_ANDROID + "citySections.json";
}
