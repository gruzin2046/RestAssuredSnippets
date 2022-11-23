package config;

public enum VideoGamesEndpoint {

    ALL_VIDEO_GAMES("videogames"),
    SINGLE_VIDEO_GAME("videogames/{videoGameId}");

    VideoGamesEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    final String endpoint;
}
