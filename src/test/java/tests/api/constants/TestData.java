package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestData {
    INVALID_BIRTHDAY("17-08-2025"),
    INVALID_INZ("validationTest"),
    INVALID_LAST_NAME("Имя");

    private final String value;
}
