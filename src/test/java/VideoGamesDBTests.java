import config.VideoGameConfig;
import org.junit.Test;

import static config.VideoGamesEndpoints.*;
import static io.restassured.RestAssured.*;

public class VideoGamesDBTests extends VideoGameConfig {

    @Test
    public void getAllGames() {
        get(ALL_VIDEO_GAMES.getEndpoint());
    }
}
