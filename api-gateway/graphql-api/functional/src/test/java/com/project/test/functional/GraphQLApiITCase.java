package com.project.test.functional;


import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import graphql.ExecutionInput;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GraphQLApiITCase {

  // needed because of Vert.x GraphQL bug, where content type with charset is considered invalid
  private static RestAssuredConfig charsetConfig = config()
      .encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

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

  @Test
  @DisplayName("Expect appropriate book from \"book\" query with a given id")
  void callGraphQLRouteWithBookQuery() {
    String query = "{\"query\": \"{"
        + "    book(id: \\\"q5NoDwAAQBAJ\\\") {"
        + "        title "
        + "        publisher "
        + "        authors "
        + "    }"
        + "}\"}";

    JsonObject book = callGraphQL(query).getJsonObject("book");

    assertThat(book.getString("title"), is(equalTo("Learning GraphQL")));
    assertThat(book.getString("publisher"), is(equalTo("\"O'Reilly Media, Inc.\"")));
    assertThat(book.getJsonArray("authors").getList(),
        is(equalTo(Arrays.asList("Eve Porcello", "Alex Banks"))));
  }

  @Test
  @DisplayName("Expect a proper list of books from \"books\" query")
  void callGraphQLRouteWithBooksQuery() {
    String query = "{\"query\": \"{"
        + "    books(match: \\\"java\\\") {"
        + "        title "
        + "        publisher "
        + "        authors "
        + "    }"
        + "}\"}";

    JsonArray books = callGraphQL(query).getJsonArray("books");

    assertThat(books.size(), is(equalTo(10)));
  }

  private JsonObject callGraphQL(String query) {
    Map response = given()
        .port(8092)
        .config(charsetConfig)
        .contentType(ContentType.JSON)
        .body(query)
        .when()
        .post("/api/graphql")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(Map.class);

    return new JsonObject(response).getJsonObject("data");
  }
}
