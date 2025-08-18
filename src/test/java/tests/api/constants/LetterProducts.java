package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LetterProducts {

    /**
     * Взятие крови из вены
     */
    VEN("VEN");

    private final String value;
}
