package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.lessThan;

public class VideoGameConfig {

    public static RequestSpecification videoGameRequestSpecification;
    public static ResponseSpecification videoGameResponseSpecification;

    @BeforeClass
    public static void setUp() {
        videoGameRequestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setBasePath("/app/")
                .setPort(8080)
                .setContentType("application/json")
                .setAccept("application/json")
              //.addHeader("Content-Type", "application/json")
              //.addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        videoGameResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000L))
                .build();

        // hook it to RestAssured class
        RestAssured.requestSpecification = videoGameRequestSpecification;
        RestAssured.responseSpecification = videoGameResponseSpecification;

    }


}
