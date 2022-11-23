import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthTests {

    @BeforeClass
    // send req through proxy
    public static void setUp() {
        RestAssured.proxy("localhost", 8888);
    }

    // only syntax with dummy data without real data testing
    // when authorisation is happening, Authorisation header need to be added to the request
    @Test
    public void basicPreemptiveTest() {
        given()
                .auth().preemptive().basic("username", "password").
        when()
                .get("http://localhost:8080/someEndpoint");
    }

    @Test
    public void basicChallengeTest() {
        given()
                .auth().basic("username", "password").
        when()
                .get("http://localhost:8080/someEndpoint");
    }

    @Test
    public void oauth1Test() {
        given()
                .auth().oauth("consumerKey", "consumerSecret",
                        "accessToken", "secretToken").
        when()
                .get("http://localhost:8080/someEndpoint");
    }

    @Test
    public void auth2Test() {
        given()
                .auth().oauth2("accessToken").
        when()
                .get("http://localhost:8080/someEndpoint");
    }

    // when some (?) problems with Https auth appears, rest assured provide us relaxedHTTPSValidation()

    @Test
    public void relaxedHTTPSTest() {
        given()
                .relaxedHTTPSValidation().
        when()
                .get("http://localhost:8080/someEndpoint");
    }

    @Test
    public void keyStoreTest() {
        given()
                .keyStore("pathToJks", "password").
        when()
                .get("http://localhost:8080/someEndpoint");
    }
}
