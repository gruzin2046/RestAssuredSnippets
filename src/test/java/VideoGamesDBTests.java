import config.VideoGameConfig;
import org.junit.Test;

import static config.VideoGamesEndpoints.ALL_VIDEO_GAMES;
import static config.VideoGamesEndpoints.SINGLE_VIDEO_GAME;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class VideoGamesDBTests extends VideoGameConfig {

    // 1.a. GET method (all collection)
    // Request URI: http://localhost:8080/app/videogames
    @Test
    public void getAllGames() {
        get(ALL_VIDEO_GAMES.getEndpoint());
    }

    // 1.b. GET method (single item) using pathParam(String paramName,Object paramValue)
    // Request URI: http://localhost:8080/app/videogames/{videoGameId} ->
    //              http://localhost:8080/app/videogames/1

    @Test
    public void getSingleGame() {
        given()
                .pathParam("videoGameId", 1).
        when()
                .get(SINGLE_VIDEO_GAME.getEndpoint()).
        then();
    }

    // 2.a. POST method - JSON format
    @Test
    public void addNewGameByJSON() {
        String requestBodyJSON = "{\"id\": 99,\n" +
                "            \"name\": \"Lord of the rings\",\n" +
                "            \"releaseDate\": \"2022-11-04T15:43:34.800Z\",\n" +
                "            \"reviewScore\": 10,\n" +
                "            \"category\": \"fantasy\",\n" +
                "            \"rating\": \"Universal\"}";

        given()
                .body(requestBodyJSON).
        when()
                .post(ALL_VIDEO_GAMES.getEndpoint()).
        then();


        /*
            {
            "id": 99,
            "name": "Lord of the rings",
            "releaseDate": "2022-11-04T15:43:34.800Z",
            "reviewScore": 10,
            "category": "fantasy",
            "rating": "Universal"
            }
        */
    }

    // 2.b. POST method - XML format
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
                .accept("application/xml").
        when()
                .post(ALL_VIDEO_GAMES.getEndpoint()).
        then();
    }

    // 3. PUT method
    @Test
    public void updateGame() {
        int id = 3;
        String requestBody = "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"asdasdasd\",\n" +
                "  \"releaseDate\": \"2022-11-05T18:54:09.666Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"string\",\n" +
                "  \"rating\": \"string\"\n" +
                "}";

        given()
                .pathParam("videoGameId", 3)
                .body(requestBody).
        when()
                .put(SINGLE_VIDEO_GAME.getEndpoint()).
        then();
    }

    // 4. DELETE method
    @Test
    public void deleteGame() {
        given().
        when()
                .delete("videogames/6").
        then();

    }

    // serialization steps
    // How to send POJO (plain old java object)
    // 1. get JSON response body from get single game request call
    // 2. generate POJO using http://pojo.sodhanalibrary.com/ online tool
    // 3. prepare VideoGame java class for single game representation
    // 4. create new VideoGame instance
    // 5. send this instance as request body

    @Test
    public void testSerializationByJSON() {
        VideoGame videoGame = new VideoGame("99", "2010", "awesome game", "any", "44", "shooter");

        given()
                .body(videoGame).
        when()
                .post(ALL_VIDEO_GAMES.getEndpoint()).
        then();

        // response:

        // {
        //    "status": "Record Added Successfully"
        // }
    }
}
