package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumericProducts {

    /**
     * Глюкоза (в крови) (Glucose)
     */
    GLUCOSE("16");

    private final String value;
}
