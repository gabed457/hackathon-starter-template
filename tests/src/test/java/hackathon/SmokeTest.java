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
    public void examplesEndpointReturnsData() {
        given()
        .when()
            .get("/api/examples")
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].name", equalTo("Alice"));
    }

    @Test
    public void predictEndpointShowsMethods() {
        given()
        .when()
            .get("/api/predict")
        .then()
            .statusCode(200)
            .body("methods", notNullValue());
    }

    @Test
    public void canGetLivePrediction() {
        given()
            .contentType("application/json")
            .body("{\"score\": 0.9}")
        .when()
            .post("/api/predict")
        .then()
            .statusCode(200)
            .body("prediction", equalTo(1))
            .body("label", equalTo("high"));
    }

    @Test
    public void homePageShowsBothMLPaths() {
        given()
        .when()
            .get("/")
        .then()
            .statusCode(200)
            .body(containsString("Pre-computed Predictions"))
            .body(containsString("Live ONNX Prediction"));
    }

    // ========================================================================
    // Example tests — uncomment and modify for your API endpoints
    // ========================================================================
}
