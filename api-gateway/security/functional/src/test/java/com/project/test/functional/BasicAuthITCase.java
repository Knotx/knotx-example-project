package com.project.test.functional;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicAuthITCase {

  @Test
  @DisplayName("GIVEN no authorization WHEN call basicAuth API EXPECT Unauthorized")
  void givenNoAuthorizationWhenCallBasicAuthApiExpectUnauthorized() {
    // @formatter:off
    given()
        .port(8092)
      .when()
        .get("/api/secure/basic")
      .then()
        .assertThat()
        .statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("GIVEN authorization WHEN call basicAuth API EXPECT Ok")
  void givenAuthorizationWhenCallBasicAuthApiExpectOk() {
    // @formatter:off
    given()
        .port(8092)
        .header("Authorization", "Basic am9objpzM2NyM3Q=")
      .when()
        .get("/api/secure/basic")
      .then()
        .assertThat()
        .statusCode(200);
    // @formatter:on
  }
}
