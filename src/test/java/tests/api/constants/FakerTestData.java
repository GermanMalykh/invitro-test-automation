package tests.api.constants;

import net.datafaker.Faker;

import java.util.Locale;

public class FakerTestData {
    private final Faker faker = new Faker(Locale.forLanguageTag("ru"));

    public String lastName = faker.name().lastName();
    public String birthDate = faker.timeAndDate().birthday(0, 120, "yyyy-MM-dd");
    public String inz = faker.numerify("#########");
    public String guid = faker.internet().uuid();

}
