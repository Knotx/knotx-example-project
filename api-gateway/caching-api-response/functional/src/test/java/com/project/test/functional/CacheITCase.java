package com.project.test.functional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class CacheITCase {

  private WireMockServer wireMockServer = new WireMockServer(options()
      .port(8080)
      .usingFilesUnderDirectory("../../common-services/webapi")
      .extensions(new ResponseTemplateTransformer(true)));

  @BeforeEach
  public void setup() {
    wireMockServer.start();
    wireMockServer.stubFor(get(urlEqualTo("/product/id"))
        .willReturn(aResponse().withStatus(200)
            .withTransformers("response-template")
            .withBodyFile("product.json")));
  }

  @Test
  @DisplayName("Expect the same response when invoking API twice.")
  void callProxyApiAndExpectCachedResponse() {
    // @formatter:off
    String firstReponse =
      given()
        .port(8092)
      .when()
        .get("/product/id")
      .then()
        .statusCode(200)
        .extract()
        .asString();

    String secondRespone =
      given()
        .port(8092)
      .when()
        .get("/product/id")
      .then()
        .statusCode(200)
        .extract()
        .asString();

    Assert.assertEquals(firstReponse, secondRespone);
    // @formatter:on
  }

}
