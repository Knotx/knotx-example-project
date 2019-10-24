package io.knotx.example.swagger.handler;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.StaticHandler;

/**
 * Serves static resources. Configuration:
 * <code>
 *   name = static
 *   config.webroot = path/to/resource
 * </code>
 */
public class StaticHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "static";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return StaticHandler.create(config.getString("webroot"));
  }
}
