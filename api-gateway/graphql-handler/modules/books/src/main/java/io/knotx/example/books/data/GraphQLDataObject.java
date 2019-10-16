package io.knotx.example.books.data;

import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.json.JsonObject;
import java.io.Serializable;

public interface GraphQLDataObject extends Serializable {
  void fromJson(JsonObject json, DataFetchingEnvironment environment);
}
