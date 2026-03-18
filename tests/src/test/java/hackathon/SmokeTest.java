package hackathon;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Smoke tests for the hackathon application.
 *
 * Prerequisites:
 *   - app.db must exist at the repo root (created by the data engineer)
 *   - The Next.js app must be running: cd app && npm run dev
 *
 * Run with: cd tests && mvn test
 */
public class SmokeTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:3000";
    }

    @Test
    public void appIsRunning() {
        given()
        .when()
            .get("/")
        .then()
            .statusCode(200);
    }

    @Test
    public void predictEndpointResponds() {
        given()
        .when()
            .get("/api/predict")
        .then()
            .statusCode(200)
            .body("status", equalTo("predict endpoint ready"));
    }

    @Test
    public void examplesEndpointReturnsData() {
        given()
        .when()
            .get("/api/examples")
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].name", equalTo("Alice"));
    }

    // ========================================================================
    // Example tests — uncomment and modify for your API endpoints
    // ========================================================================

    // @Test
    // public void postPrediction() {
    //     String body = "{\"features\": [1.0, 2.5, 3.0]}";
    //
    //     given()
    //         .contentType("application/json")
    //         .body(body)
    //     .when()
    //         .post("/api/predict")
    //     .then()
    //         .statusCode(200)
    //         .body("prediction", notNullValue());
    // }
}
