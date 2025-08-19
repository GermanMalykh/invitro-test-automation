package tests.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Capitals {
    MOSCOW("Москва", "RUSSIA"),
    YEREVAN("Ереван", "ARMENIA"),
    MINSK("Минск", "BELARUS"),
    ASTANA("Астана", "KAZAKHSTAN");

    private final String cityName;
    private final String countryName;

}
