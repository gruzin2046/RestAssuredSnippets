import config.VideoGameConfig;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static config.VideoGamesEndpoint.ALL_VIDEO_GAMES;
import static io.restassured.RestAssured.given;

public class GPathXMLTests extends VideoGameConfig {

    // Content list
    // 1. get first game using index inside path() method calling on response
    // 2. get attribute using @ mark
    // 3. get all XML node list, get info from selected node
    // 4. get all XML node list based on conditions
    // 5. get all XML node list, then filter it by attribute name
    // 6. get single Node by its child node (in this case by "name"), than extract "releaseDate" from it
    // 7. get single Node by its child node (in this case by "name"), than extract "releaseDate" from it
    //    *using DepthSearch

    // private method to get all games in XML format
    private static Response getAllGames() {
        return given()
                .contentType("application/xml")
                .accept("application/xml").
                when()
                .get(ALL_VIDEO_GAMES.getEndpoint());
    }

    // response body in XML format

    //<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    //<videoGames>
    //  <videoGame category="Shooter" rating="Universal">
    //    <id>1</id>
    //    <name>Resident Evil 4</name>
    //    <releaseDate>2005-10-01T00:00:00+02:00</releaseDate>
    //    <reviewScore>85</reviewScore>
    //  </videoGame>
    //  <videoGame category="Driving" rating="Universal">
    //    <id>2</id>
    //    <name>Gran Turismo 3</name>
    //    <releaseDate>2001-03-10T00:00:00+01:00</releaseDate>
    //    <reviewScore>91</reviewScore>
    //  </videoGame>
    //  (...)
    //</videoGames>

    // 1. get first game using index inside path() method calling on response
    @Test
    public void getFirstGameName() {
        String firstGameName = getAllGames().path("videoGames.videoGame.name[0]");
        String expectedName = "Resident Evil 4";
        Assert.assertEquals(expectedName, firstGameName);
    }

    // 2. get attribute using @ mark
    // * as we can see in the response above "category" and "rating" are attributes inside "videoGame" tag
    //   ->  this is the main difference between JSON and XML format
    @Test
    public void getAttribute() {
        String firstGameCategory = getAllGames().path("videoGames.videoGame[0].@category");
        String expectedCategory = "Shooter";
        Assert.assertEquals(expectedCategory, firstGameCategory);
    }

    // 3. get all XML node list, get info from selected node
    @Test
    public void getAllNodes() {
        String responseAsString = getAllGames().asString();
        List<Node> allGames = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { element -> return element } "
        );

        String categoryOfSecondGame = allGames.get(1).getAttribute("category");
        String expectedCategory = "Driving";
        Assert.assertEquals(expectedCategory, categoryOfSecondGame);

        String nameOfTheSecondGame = allGames.get(1).get("reviewScore").toString();
        String expectedName = "91";
        Assert.assertEquals(expectedName, nameOfTheSecondGame);
    }

    // 4. get all nodes based on conditions
    @Test
    public void getAllNodesMatchingCertainCriteria() {
        String responseAsString = getAllGames().asString();
        float reviewScore = 93;
        List<Node> allVideoGamesOverCertainScore = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { it.reviewScore.toFloat() >= " + reviewScore + "}"
        );
        List<String> allDrivingGamesNames = allVideoGamesOverCertainScore.stream()
                .map(game -> game.get("name").toString())
                .collect(Collectors.toList());

        List<String> expectedGamesNames = List.of("The Legend of Zelda: Ocarina of Time", "Final Fantasy VII");
        Assert.assertEquals(expectedGamesNames, allDrivingGamesNames);

    }

    // 5. get all XML nodes, then filter it by attribute name
    @Test
    public void getListOfNodesUsingFindAllOnAttribute() {
        String responseAsString = getAllGames().asString();
        List<Node> allDrivingGames = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { game -> def category = game.@category; category == 'Driving' } "
        );

        List<String> allDrivingGamesNames = allDrivingGames.stream()
                .map(game -> game.get("name").toString())
                .collect(Collectors.toList());

        List<String> expectedGamesNames = List.of("Gran Turismo 3", "Grand Theft Auto III");
        Assert.assertEquals(expectedGamesNames, allDrivingGamesNames);
    }

    // 6. get single Node by its child node (in this case by "name"), than extract "releaseDate" from it
    @Test
    public void getSingleNodeReleaseDateBasedOnName() {
        String responseAsString = getAllGames().asString();
        String videoGameReleaseDate = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.find { game -> def name = game.name; name == 'Tetris' }.releaseDate "
        );

        String expectedDate = "1984-06-25T00:00:00+02:00";
        Assert.assertEquals(expectedDate, videoGameReleaseDate);
    }

    // 7. get single Node by its child node (in this case by "name"), than extract "releaseDate" from it
    //    *using DepthSearch
    @Test
    public void getSingleNodeReleaseDateBasedOnNameUsingDepthSearch() {
        String responseAsString = getAllGames().asString();
        String videoGameReleaseDate = XmlPath.from(responseAsString).get(
                "**.find { it.name == 'Tetris' }.releaseDate "
        );

        String expectedDate = "1984-06-25T00:00:00+02:00";
        Assert.assertEquals(expectedDate, videoGameReleaseDate);
    }


}
