package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    CONFLICT("Conflict"),
    EXCEEDED_REQUESTS("Too Many Requests"),
    BAD_REQUEST("Bad Request"),
    INTERNAL_ERROR("Internal Server Error");

    private final String value;
}
