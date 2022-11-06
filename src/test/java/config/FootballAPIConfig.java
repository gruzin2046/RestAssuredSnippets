package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

public class FootballAPIConfig {

    public static RequestSpecification footballRequestSpecification;
    public static ResponseSpecification footballResponseSpecification;

    @BeforeClass
    public static void setUp() {
        footballRequestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.football-data.org")
                .setBasePath("/v4/")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addHeader("X-Auth-Token", "7d3a989e43d5404b9968d48b2219c5cf")
                .addHeader("X-Response-Control", "minified")
                .build();

        footballResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = footballRequestSpecification;
        RestAssured.responseSpecification = footballResponseSpecification;
    }
}
