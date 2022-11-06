import config.FootballAPIConfig;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class FootballApiTests extends FootballAPIConfig {

    //query params

    @Test
    public void getDetailsOfOneArea() {
        given()
                .queryParam("areas", 2072).
        when()
                .get("/areas").
        then();

    }
}
