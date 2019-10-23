package io.knotx.examples.security.auth;

import io.knotx.server.api.security.AuthHandlerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.shiro.ShiroAuth;
import io.vertx.reactivex.ext.web.handler.AuthHandler;
import io.vertx.reactivex.ext.web.handler.BasicAuthHandler;

public class BasicAuthHandlerFactory implements AuthHandlerFactory {

  @Override
  public String getName() {
    return "helloBasicAuthFactory";
  }

  @Override
  public AuthHandler create(Vertx vertx, JsonObject config) {
    final ShiroAuth shiroAuth = ShiroAuth.create(vertx, new ShiroAuthOptions().setConfig(config));
    return BasicAuthHandler.create(shiroAuth);
  }
}
