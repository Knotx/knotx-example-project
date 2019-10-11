package com.project.test.functional;


import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HelloApiITCase {

  @Test
  @DisplayName("Expect 200 status code from hello api.")
  void callHandlersApiEndpointAndExpectOK() {
    // @formatter:off
    given().
        port(8092).
      when().
        get("/api/hello").
      then()
        .assertThat().
        statusCode(200);
    // @formatter:on
  }

}
