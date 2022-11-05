import config.VideoGameConfig;
import org.junit.Test;

import static config.VideoGamesEndpoints.ALL_VIDEO_GAMES;
import static config.VideoGamesEndpoints.SINGLE_VIDEO_GAME;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class VideoGamesDBTests extends VideoGameConfig {

    @Test
    public void getAllGames() {
        get(ALL_VIDEO_GAMES.getEndpoint());
    }

    @Test
    public void addNewGameByJSON() {
        String requestBodyJSON = "{\"id\": 100,\n" +
                "            \"name\": \"Lord of the rings\",\n" +
                "            \"releaseDate\": \"2022-11-04T15:43:34.800Z\",\n" +
                "            \"reviewScore\": 10,\n" +
                "            \"category\": \"fantasy\",\n" +
                "            \"rating\": \"Universal\"}";

        given()
                .body(requestBodyJSON)
                .when()
                .post(ALL_VIDEO_GAMES.getEndpoint())
                .then();


        /*{
            "id": 99,
            "name": "Lord of the rings",
            "releaseDate": "2022-11-04T15:43:34.800Z",
            "reviewScore": 10,
            "category": "fantasy",
            "rating": "Universal"
            }*/
    }

    @Test
    public void addNewGameByXML() {
        String requestBody = "  <videoGame category=\"Shooter\" rating=\"Universal\">\n" +
                "    <id>19</id>\n" +
                "    <name>Resident Evil 8</name>\n" +
                "    <releaseDate>2005-10-01T00:00:00+01:00</releaseDate>\n" +
                "    <reviewScore>99</reviewScore>\n" +
                "  </videoGame>";
        given()
                .body(requestBody)
                .contentType("application/xml")
                .accept("application/xml")
                .when()
                .post(ALL_VIDEO_GAMES.getEndpoint())
                .then();
    }

    @Test
    public void updateGame() {
        String id = "2";
        String requestBody = "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"asdasdasd\",\n" +
                "  \"releaseDate\": \"2022-11-05T18:54:09.666Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"string\",\n" +
                "  \"rating\": \"string\"\n" +
                "}";

        String endpoint = SINGLE_VIDEO_GAME
                .getEndpoint()
                .replace("{videoGameId}", id);

        given()
                .body(requestBody)
                .when()
                .put(endpoint)
                .then();
    }
}
