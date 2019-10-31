package io.knotx.example.swagger.handler;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Serves all configuration under `json` key as a static JSON resource.
 * Can consume either object or array, supports nesting.
 */
public class StaticJsonHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "static-json";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return routingContext -> {
      Object json = config.getMap().get("json");
      routingContext.response().end(encodeJson(json));
    };
  }

  private String encodeJson(Object object) {
    if (object instanceof JsonObject) {
      return ((JsonObject) object).encode();
    } else if (object instanceof JsonArray) {
      return ((JsonArray) object).encode();
    } else {
      throw new IllegalStateException("Can't encode non-json object as JSON");
    }
  }
}
