package config;

public enum VideoGamesEndpoints {

    ALL_VIDEO_GAMES("videogames"),
    SINGLE_VIDEO_GAME("videogames/{videoGameId}");

    VideoGamesEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    final String endpoint;
}
