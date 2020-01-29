package io.knotx.example.hellohandler;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.oauth2.KeycloakHelper;
import io.vertx.reactivex.ext.web.RoutingContext;

public class HelloWorldOpenIDHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "helloOpenIdHandler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return event -> {
      String name = KeycloakHelper.idToken(event.user().principal())
          .getString("name");
      event.response().end("Hello " + name);
    };
  }
}