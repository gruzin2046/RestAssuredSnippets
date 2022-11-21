import config.FootballAPIConfig;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GPathJSONTests extends FootballAPIConfig {

    // Content list ->
    // Usage of 'find' and 'it' inside groovy path:
    // 1. Find all data matching certain criteria
    // 2. Find single data matching certain criteria
    // 3. Find list of data matching certain criteria
    // 4. Difference between usage of find() and findAll() methods
    // 5. Find list of data matching multiple criteria
    // 6. Find list of data matching certain criteria (>, <, >=, <=, min, max, collect, sum)

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
        List<String> expectedPlayers = List.of("Gabriel", "Marquinhos", "Gabriel Jesus", "Martinelli");

        Assert.assertEquals(expectedPlayers, playersFromBrazil);
    }

    // 4. findAll() method returns list of elements - if we will use find() in the same place instead -
    //    we will receive first element of the list:

    @Test
    public void checkFindWhenItCanFindListOfElements() {
        Response response = FootballApiTests.getArsenalDataResponse();
        String firstPlayerFromBrazilPlayersList = response
                .path("squad.find { it.nationality == 'Brazil' }.name ");
        String expectedPlayer = "Gabriel";

        Assert.assertEquals(expectedPlayer, firstPlayerFromBrazilPlayersList);
    }

    // 5. Find list of data matching multiple criteria
    //    In this case we are looking for list of Arsenal players names from Brazil and playing in offence
    @Test
    public void extractListOfDataForMultipleConditions() {
        Response response = FootballApiTests.getArsenalDataResponse();
        String nationality = "Brazil";
        String position = "Offence";
        List<String> offencePlayersFromBrazil = response
                .path("squad.findAll { it.nationality == '%s' }.findAll { it.position == 'Offence'}.name ",
                        nationality, position);
        List<String> expectedPlayers = List.of("Gabriel Jesus", "Martinelli");

        Assert.assertEquals(expectedPlayers, offencePlayersFromBrazil);
    }

    // 6. Find list of data matching certain criteria (>, <, >=, <=, min, max, collect, sum)
    //    In this case we are looking for list of Arsenal players names which id number is less than 5000
    @Test
    public void extractListOfDataForMathCondition() {
        Response response = FootballApiTests.getArsenalDataResponse();
        List<String> namesOfPlayersWithIdLessThan1000 = response
                .path("squad.findAll { it.id >= 90000 }.name ");
        List<String> expectedPlayers = List.of("Fabio Vieira", "Marquinhos", "Bukayo Saka");

        Assert.assertEquals(expectedPlayers, namesOfPlayersWithIdLessThan1000);
    }

    @Test
    public void extractDataWithHighestValue() {
        Response response = FootballApiTests.getArsenalDataResponse();
        String nameOfPlayerWithMaxIdNumber = response.path("squad.max { it.id }.name ");
        String expectedPlayer = "Marquinhos";

        Assert.assertEquals(expectedPlayer, nameOfPlayerWithMaxIdNumber);
    }

    @Test
    public void extractSumOfDataMatchingCertainCriteria() {
        Response response = FootballApiTests.getArsenalDataResponse();
        int sumOfSquadIds = response.path("squad.collect { it.id }.sum() ");
        int expectedNumber = 807009;

        Assert.assertEquals(expectedNumber, sumOfSquadIds);
    }

}
