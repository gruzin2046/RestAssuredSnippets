import config.FootballAPIConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class FootballApiTests extends FootballAPIConfig {

    public static Response getArsenalDataResponse() {
        return get("/teams/57").then()
                        .contentType(ContentType.JSON)
                        .extract().response();
    }

    // 1. use of query params

    @Test
    public void getDetailsOfOneArea() {
        // https://api.football-data.org/v4/areas?areas=2072
        given()
                .queryParam("areas", 2072).
        when()
                .get("/areas").
        then();
    }

    // 2. assert part of the response

    @Test
    public void getDateFounded() {
        given().
        when()
                .get("/teams/57").
        then()
                .body("founded", equalTo(1886));
    }

    @Test
    public void getFirstTeamName() {
        when()
                .get("competitions/2021/teams").
        then()
                .body("teams.name[0]", equalTo("Arsenal FC"));
    }

    // 3. extract response

    @Test
    public void checkTheResponse() {
        Response response = getArsenalDataResponse();

        // 3.a. extract list of all nationalities using response.path()
        List<String> nationalities = response.path("squad.nationality");

        // print all different nationalities of a team players
        nationalities.stream().distinct().forEach(System.out::println);

    }


    // Extract list of running competitions from JSON

    /*
    "id": 57,
    "name": "Arsenal FC",
    "shortName": "Arsenal",
    "tla": "ARS",
    "crest": "https://crests.football-data.org/57.png",
    "address": "75 Drayton Park London N5 1BU",
    "website": "http://www.arsenal.com",
    "founded": 1886,
    "clubColors": "Red / White",
    "venue": "Emirates Stadium",
    "runningCompetitions": [
        {
            "id": 2021,
            "name": "Premier League",
            "code": "PL",
            "type": "LEAGUE",
            "emblem": "https://crests.football-data.org/PL.png"
        },
        {
            "id": 2139,
            "name": "Football League Cup",
            "code": "FLC",
            "type": "CUP",
            "emblem": "https://crests.football-data.org/FLC.png"
        },
        {
            "id": 2146,
            "name": "UEFA Europa League",
            "code": "EL",
            "type": "CUP",
            "emblem": "https://crests.football-data.org/EL.png"
        }
    ]*/

    // 3.b. Extract list of running competitions from JSON using Response.jsonPath().getList()
    @Test
    public void getListOfRunningCompetitions() {
        List<String> competitionNames =
                get("/teams/57").jsonPath().getList("runningCompetitions.name");


        List<String> expectedList = Arrays.asList("Premier League", "Football League Cup", "UEFA Europa League");
        Assert.assertEquals(expectedList, competitionNames);
    }

    // get list of players names and check if it contains expected one
    @Test
    public void checkIfPlayerIsInTheTeam() {
        List<String> playersNames =
                get("/teams/57").jsonPath().getList("squad.name");
        boolean isInTheTeam = playersNames.stream().anyMatch(s -> s.equals("Gabriel Jesus"));
        Assert.assertTrue(isInTheTeam);
    }

    // 4. extract headers from response
    @Test
    public void extractHeaders() {
        Response response = getArsenalDataResponse();
        Headers headers = response.getHeaders();

        List<Header> headersNames = headers.asList();
        System.out.println(headersNames);
    }
}
