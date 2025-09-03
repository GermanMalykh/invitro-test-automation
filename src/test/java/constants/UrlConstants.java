package constants;

/**
 * Централизованные URL константы для всего проекта.
 * Содержит все базовые URL и производные ссылки
 */
public class UrlConstants {

    // Базовые URL для разных сервисов
    public static final String INVITRO_MAIN = "https://invitro.ru/";
    public static final String INVITRO_API = "https://lk3.invitro.ru";
    public static final String INVITRO_MOBILE_API = "https://lk3-mobile-api.invitro.ru";
    
    // Конкретные URL для Web тестов
    public static final String CART_URL = INVITRO_API + "/cart";
    public static final String COOKIE_CONFIG_URL = INVITRO_API + "/assets/edna-banner-close.svg";

    // User Agent строки
    public static final String USER_AGENT_DESKTOP = "Mozilla/999.999 (Macintosh; Intel Mac OS X 99_99_99) Chrome/999.999.999.999 Safari/999.999";
    public static final String USER_AGENT_MOBILE = "okhttp/4.9.3 (Android) InvitroApp";
}
