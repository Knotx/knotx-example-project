package io.knotx.example.books.data;

import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.lang.reflect.Array;
import java.util.function.Function;

public class TaskArrayDataFetcher<T extends GraphQLDataObject> extends TaskDataFetcher<T[]> {

  private Class<T> clazz;
  private Function<JsonObject, JsonArray> toArray;

  public TaskArrayDataFetcher(String task, Class<T> clazz, Vertx vertx, JsonObject config, RoutingContext routingContext, Function<JsonObject, JsonArray> toArray) {
    super(vertx, config, routingContext, task);
    this.clazz = clazz;
    this.toArray = toArray;
  }

  @Override
  T[] getDataObjectFromJson(JsonObject json, DataFetchingEnvironment environment) throws IllegalAccessException, InstantiationException {
    JsonArray jsonArray = toArray.apply(json);

    T[] dataArray = (T[]) Array.newInstance(clazz, jsonArray.size());

    for (int i = 0; i < jsonArray.size(); i ++) {
      T dataObject = clazz.newInstance();
      dataObject.fromJson(jsonArray.getJsonObject(i), environment);
      dataArray[i] = dataObject;
    }

    return dataArray;
  }
}
