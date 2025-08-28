package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;

public class RestSpec {

    public static RequestSpecification requestSpecification = with()
            .filter(withCustomTemplates())
            .log().all();

    public static ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(ALL)
            .build();
}
