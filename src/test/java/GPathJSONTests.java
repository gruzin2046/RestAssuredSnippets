import config.FootballAPIConfig;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GPathJSONTests extends FootballAPIConfig {

    // Content list ->
    // Usage of 'find' and 'it' inside groovy path:
    // 1. Find all data matching certain criteria
    // 2. Find single data matching certain criteria
    // 3. Find list of data matching certain criteria

    // 1. Find all data using 'find' and 'it' inside groovy path
    //    In this case we are looking for all data for team name = "Manchester United FC"
    @Test
    public void extractMapOfElementsWithFind() {
        // get all teams from 2021 competitions
        Response response = get("competitions/2021/teams");

        // store all Manchester data in a map for team name = "Manchester United FC":
        // we are using groovy path, 'find' method is looking inside teams array,
        // 'it' points all data for match: name == 'Manchester United FC'

        Map<String, ?> allDataOfManchesterUnitedFC = response
                .path("teams.find { it.name == 'Manchester United FC' }");
    }

    // 2. Find single data matching certain criteria
    //    In this case we are looking for dateOfBirth field of player named "Kieran Tierney"
    @Test
    public void extractSingleData() {
        Response response = FootballApiTests.getArsenalDataResponse();
        String certainPlayerDateOfBirth = response
                .path("squad.find { it.name == 'Kieran Tierney' }.dateOfBirth");
        String expectedDateOfBirth = "1997-06-05";

        Assert.assertEquals(expectedDateOfBirth, certainPlayerDateOfBirth);
    }

    // 3. Find list of data matching certain criteria
    //    In this case we are looking for list of Arsenal players names from Brazil
    @Test
    public void extractListOfData() {
        Response response = FootballApiTests.getArsenalDataResponse();
        List<String> playersFromBrazil = response
                .path("squad.findAll { it.nationality == 'Brazil' }.name ");
        List<String> expectedBrazilPlayers = List.of("Gabriel", "Marquinhos", "Gabriel Jesus", "Martinelli");

        Assert.assertEquals(expectedBrazilPlayers, playersFromBrazil);
    }

}
