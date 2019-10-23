package io.knotx.examples.security.routing;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class OAuth2HelloHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "oauth2-hello-handler-factory";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return new OAuth2HelloHandler(vertx, config);
  }
}
