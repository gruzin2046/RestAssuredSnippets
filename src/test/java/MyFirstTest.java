import config.VideoGameConfig;
import org.junit.Test;

import static config.VideoGamesEndpoints.*;
import static io.restassured.RestAssured.*;

// 1. test class needs to extend TestConfig if we want to use @BeforeClass setUp() method
public class MyFirstTest extends VideoGameConfig {

    @Test
    public void myFirstTest() {
        given()
                .log().all().
        when()
                .get("videogames").
        then()
                .log().all();
    }

    @Test
    public void myFirstTestWithEndpoint() {
        // 2. given().when().then() structure is just syntactic sugar
        // we can simplify test and use get() request without it
        get(ALL_VIDEO_GAMES.getEndpoint()).then().log().all();
    }
}
