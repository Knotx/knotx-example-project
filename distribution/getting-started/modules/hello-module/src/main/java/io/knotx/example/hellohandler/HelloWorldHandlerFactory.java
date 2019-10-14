package io.knotx.example.hellohandler;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class HelloWorldHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "hellohandler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    String message = config.getString("message", "Some default message");
    JsonObject jsonObject = new JsonObject()
        .put("message", message);
    return event -> event.response().end(jsonObject.toString());
  }
}