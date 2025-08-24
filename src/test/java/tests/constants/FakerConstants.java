package tests.constants;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FakerConstants {
    private final Faker faker = new Faker(Locale.forLanguageTag("ru"));

    public String lastName = faker.name().lastName();
    public String birthDate = faker.timeAndDate().birthday(0, 120, "yyyy-MM-dd");
    public String birthDateAndroid = faker.timeAndDate().birthday(0, 120, "dd.MM.yyyy");
    public String inz = faker.numerify("#########");
    public String guid = faker.internet().uuid();

    public String birthDateAndroid(String birthDate) {
        LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
