package io.knotx.example.swagger.handler;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Responses with a given status code. Example configuration:
 * <code>
 *   name = static-status
 *   config.code = 200
 * </code>
 */
public class StaticStatusHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "static-status";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return routingContext -> routingContext
        .response()
        .setStatusCode(config.getInteger("code"))
        .end();
  }
}
