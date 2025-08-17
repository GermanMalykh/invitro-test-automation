package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    EXCEEDED_MESSAGE("Вы израсходовали количество попыток, возвращайтесь позже"),

    EMPTY_MESSAGE(""),
    BAD_REQUEST_MESSAGE("400 BAD_REQUEST \"Validation failure\"");

    private final String value;

}
