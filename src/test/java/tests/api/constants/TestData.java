package tests.api.constants;

import net.datafaker.Faker;

import java.util.Locale;

public class TestData {
    Faker faker = new Faker(Locale.forLanguageTag("ru"));
    public String lastName = faker.name().lastName();
    public String birthDate = faker.timeAndDate().birthday(18, 65, "yyyy-MM-dd");
    public String inz = faker.numerify("#########");
}
