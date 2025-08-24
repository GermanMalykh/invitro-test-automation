package tests.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IntConstants {
    MAX_LIMIT_ATTEMPTS(4);

    private final int value;
}
