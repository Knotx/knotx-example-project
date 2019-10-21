package io.knotx.example.books.data;

import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.json.JsonObject;
import java.io.Serializable;

/**
 * Interface used for objects deserialized from JSON and passed to {@link graphql.GraphQL} via {@link TaskDataFetcher}
 */
public interface GraphQLDataObject extends Serializable {
  void fromJson(JsonObject json, DataFetchingEnvironment environment);
}
