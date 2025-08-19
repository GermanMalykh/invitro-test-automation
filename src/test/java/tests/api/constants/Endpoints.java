package tests.api.constants;

public class Endpoints {

    private final static String SITE_API_PATH = "site/api/";

    /**
     * Results Endpoint
     */
    public static final String RESULTS_PATH = SITE_API_PATH + "unauth/results";

    /**
     * Products Endpoint
     */
    public static final String PRODUCTS_PATH = SITE_API_PATH + "products";

    /**
     * Cities Endpoint
     */
    public static final String SINGLE_CITIES_PATH = SITE_API_PATH + "cities/%s";
    public static final String ALL_CITIES_PATH = SITE_API_PATH + "cities";

    /**
     * Office Endpoint
     */
    public static final String OFFICES_PATH = SITE_API_PATH + "cart/offices";

}
