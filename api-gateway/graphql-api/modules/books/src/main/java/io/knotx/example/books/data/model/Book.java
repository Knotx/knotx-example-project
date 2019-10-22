package io.knotx.example.books.data.model;

import graphql.schema.DataFetchingEnvironment;
import io.knotx.example.books.data.GraphQLDataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

public class Book implements GraphQLDataObject {

  private String title;
  private String publisher;
  private List<String> authors;

  @Override
  public void fromJson(JsonObject json, DataFetchingEnvironment environment) {
    JsonObject volumeInfo = json.getJsonObject("volumeInfo");

    title = volumeInfo.getString("title");
    publisher = volumeInfo.getString("publisher");
    authors = new LinkedList<>();

    volumeInfo.getJsonArray("authors", new JsonArray()).forEach(object -> authors.add((String) object));
  }

  public String getTitle() {
    return title;
  }

  public String getPublisher() {
    return publisher;
  }

  public List<String> getAuthors() {
    return authors;
  }
}
