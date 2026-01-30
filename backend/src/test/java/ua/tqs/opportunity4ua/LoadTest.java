package ua.tqs.opportunity4ua;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class LoadTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Disabled
    @Test
    void debug_with_logs() {
        given()
            .log().all()
        .when()
            .get("/api/opportunity")
        .then()
            .log().all();
    }

    @Disabled
    @Test
    void loadTest_getOpportunities_50Requests() {
        for (int i = 0; i < 50; i++) {
            given()
            .when()
                .get("/api/opportunity")
                .then().statusCode(anyOf(is(200), is(401)))
                .time(lessThan(1000L));
        }
    }

    @Disabled
    @Test
    void loadTest_getRewards_50Requests() {
        for (int i = 0; i < 50; i++) {
            given()
                .header("X-Auth-Token", "test-token")
            .when()
                .get("/api/rewards")
                .then().statusCode(anyOf(is(200), is(401)))
                .time(lessThan(1000L));
        }
    }
}
