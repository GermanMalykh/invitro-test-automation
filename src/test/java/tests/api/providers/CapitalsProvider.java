package tests.api.providers;

import org.junit.jupiter.params.provider.Arguments;
import tests.api.constants.Capitals;

import java.util.stream.Stream;

public class CapitalsProvider {

    public static Stream<Arguments> provideCapitalCountryPairs() {
        return Stream.of(
                Arguments.of(Capitals.MOSCOW.getCityName(), Capitals.MOSCOW.getCountryName()),
                Arguments.of(Capitals.YEREVAN.getCityName(), Capitals.YEREVAN.getCountryName()),
                Arguments.of(Capitals.MINSK.getCityName(), Capitals.MINSK.getCountryName()),
                Arguments.of(Capitals.ASTANA.getCityName(), Capitals.ASTANA.getCountryName())
        );
    }
}
