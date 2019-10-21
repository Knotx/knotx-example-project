package io.knotx.example.books.data;

import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class TaskSingleDataFetcher<T extends GraphQLDataObject> extends TaskDataFetcher<T> {

  private final Class<T> clazz;

  public TaskSingleDataFetcher(String task, Class<T> clazz, Vertx vertx, JsonObject config, RoutingContext routingContext) {
    super(vertx, config, routingContext, task);
    this.clazz = clazz;
  }

  @Override
  T getDataObjectFromJson(JsonObject json, DataFetchingEnvironment environment) throws IllegalAccessException, InstantiationException {
    T dataObject = clazz.newInstance();
    dataObject.fromJson(json, environment);
    return dataObject;
  }
}
