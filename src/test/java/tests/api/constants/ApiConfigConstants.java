package tests.api.constants;

import constants.UrlConstants;
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
    BASE_URL(UrlConstants.INVITRO_API),
    BASE_URL_MOBILE(UrlConstants.INVITRO_MOBILE_API),

    // User Agent для API запросов
    USER_AGENT(UrlConstants.USER_AGENT_DESKTOP),
    USER_AGENT_MOBILE(UrlConstants.USER_AGENT_MOBILE);

    private final String value;

}
