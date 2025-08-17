package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Константы конфигурации API
 * Централизованное место для всех API настроек
 */
@Getter
@AllArgsConstructor
public enum ApiConfigConstants {

    // Base URL для API
    BASE_URL("https://lk3.invitro.ru"),

    // User Agent для API запросов
    USER_AGENT("Mozilla/999.999 (Macintosh; Intel Mac OS X 99_99_99) Chrome/999.999.999.999 Safari/999.999");

    private final String value;

}
