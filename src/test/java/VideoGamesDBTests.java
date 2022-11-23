import config.VideoGameConfig;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static config.VideoGamesEndpoint.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

public class VideoGamesDBTests extends VideoGameConfig {

    // content list:

    // 1.a. GET method (collection)
    // 1.b. GET method (single item) using pathParam(String paramName,Object paramValue)
    // 2.b. POST method - XML format
    // 3. PUT method
    // 4. DELETE method
    // 5. How to send POJO (plain old java object)
    // 6. How to convert JSON response to POJO
    // 7. Validate response against JSON Schema.
    // 8. Validate response against XML Schema.
    // 9. Capture response time using get().time()
    // 10. Assert response time using get().then().time(lesThan(value))

    public Response getSingleGame(int id, String dataFormat) {
        String format = String.format("application/%s", dataFormat);
        return
                given()
                        .contentType(format)
                        .accept(format)
                        .pathParam("videoGameId", id).
                when()
                        .get(SINGLE_VIDEO_GAME.getEndpoint());
    }


    // 1.a. GET method (collection)
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
        String requestBody = "  <videoGame.xsd category=\"Shooter\" rating=\"Universal\">\n" +
                "    <id>19</id>\n" +
                "    <name>Resident Evil 8</name>\n" +
                "    <releaseDate>2005-10-01T00:00:00+01:00</releaseDate>\n" +
                "    <reviewScore>99</reviewScore>\n" +
                "  </videoGame.xsd>";
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

    // serialization
    // 5. How to send POJO (plain old java object)

    // 1. get JSON response body from get single game request call
    // 2. generate POJO using http://pojo.sodhanalibrary.com/ online tool
    // 3. prepare VideoGame java class for single game representation
    // 4. create new VideoGame instance
    // 5. send this instance as request body

    @Test
    public void testSerializationByJSON() {
        VideoGame videoGame = new VideoGame("99", "awesome game", "2010", "8/10", "any", "shooter");

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

    // 6. how to convert JSON response to POJO

    // 1. add empty constructor in VideoGame class
    // 2. call get method on single game
    // 3. save game using response.getBody().as(VideoGame.class);
    // 4. add equals() and hashCode() methods to assert

    @Test
    public void convertJSONtoPOJO() {
        Response response = getSingleGame(1, "json");
        VideoGame videoGame = response.getBody().as(VideoGame.class);
        VideoGame expectedVG = new VideoGame("1","Resident Evil 4",
                "2005-10-01", "85", "Shooter", "Universal");

        /*{
                "id": 1,
                "name": "Resident Evil 4",
                "releaseDate": "2005-10-01",
                "reviewScore": 85,
                "category": "Shooter",
                "rating": "Universal"
        }*/

        Assert.assertEquals(videoGame, expectedVG);
    }

    // 7. Validate response against JSON Schema. steps:

    // 1. get JSON response body from get single game request call
    // 2. generate JSON schema using https://www.liquid-technologies.com/online-json-to-schema-converter
    // 3. save schema as videoGame.json in src\main\resources\
    // 4. add import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
    // 5. call get req for single game
    // 6. assert response body using XSD schema using .body(matchesJsonSchemaInClasspath("videoGame.xsd"));


    @Test
    public void testVideoGameSchemaJSON() {
        getSingleGame(3, "json")
                .then()
                .body(matchesJsonSchemaInClasspath("videoGame.json"));
    }

    // 8. Validate response against XML Schema. steps:

    // 1. get XLM response body from get single game request call
    // 2. generate XSD using https://www.freeformatter.com/xsd-generator.html
    // 3. save XSD as videoGame.xsd in src\main\resources\
    // 4. add import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
    // 5. call get req for single game
    // 6. assert response body using XSD schema using .body(matchesXsdInClasspath("videoGame.xsd"));


    @Test
    public void testVideoGameSchemaXML() {
        given()
                .pathParam("videoGameId", 1)
                .contentType("application/xml")
                .accept("application/xml").
        when()
                .get(SINGLE_VIDEO_GAME.getEndpoint()).
        then()
                .body(matchesXsdInClasspath("videoGame.xsd"));
    }

    // 9. Capture response time using get().time();
    @Test
    public void captureResponseTime() {
        long responseTime = get(ALL_VIDEO_GAMES.getEndpoint()).time();
        System.out.println("Response time in MS: " + responseTime);
    }

    // 10. Assert response time using get().then().time(lesThan(value in milliseconds));
    // remember about: import static org.hamcrest.Matchers.lessThan;
    @Test
    public void assertResponseTime() {
        given().
        when().
                get(ALL_VIDEO_GAMES.getEndpoint()).
        then().
                time(lessThan(1000L));
    }

}
