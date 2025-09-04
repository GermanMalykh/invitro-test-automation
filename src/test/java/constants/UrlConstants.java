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

    // User Agent
    public static final String USER_AGENT_DESKTOP = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    public static final String USER_AGENT_MOBILE = "okhttp/4.9.3 (Android) InvitroApp";
}
