package com.project.test.functional;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JWTAuthITCase {

  @Test
  @DisplayName("GIVEN no authorization WHEN call JWT secured API EXPECT Unauthorized")
  void givenNoAuthorizationWhenCallBasicAuthApiExpectUnauthorized() {
    // @formatter:off
    given()
        .port(8092)
      .when()
        .get("/api/secure/jwt")
      .then()
        .assertThat()
        .statusCode(401);
    // @formatter:on
  }

  @Test
  @DisplayName("GIVEN authorization WHEN call JWT secured API EXPECT Ok")
  void givenAuthorizationWhenCallBasicAuthApiExpectOk() {
    // @formatter:off
    given()
        .port(8092)
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.vPWK59pl5GWimz8UVbL3CmrceSfmNvvCgyzwLVV9jT8")
      .when()
        .get("/api/secure/jwt")
      .then()
        .assertThat()
        .statusCode(200);
    // @formatter:on
  }
}
