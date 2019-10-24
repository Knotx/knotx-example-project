package com.project.test.functional;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExampleApiITCase {

  @Test
  @DisplayName("Expect 200 status code when endpoint registered.")
  void callUndefinedRoute() {
    given()
        .port(8092)
        .when()
        .get("/healthcheck")
        .then()
        .assertThat()
        .statusCode(200);
  }

}
