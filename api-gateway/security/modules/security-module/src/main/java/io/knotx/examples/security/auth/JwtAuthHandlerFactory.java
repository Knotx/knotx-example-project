package io.knotx.examples.security.auth;

import io.knotx.server.api.security.AuthHandlerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import io.vertx.reactivex.ext.web.handler.AuthHandler;
import io.vertx.reactivex.ext.web.handler.JWTAuthHandler;

public class JwtAuthHandlerFactory implements AuthHandlerFactory {

  @Override
  public String getName() {
    return "helloJwtAuthFactory";
  }

  @Override
  public AuthHandler create(Vertx vertx, JsonObject config) {
    PubSecKeyOptions pubSecKey = new PubSecKeyOptions(config);
    JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().addPubSecKey(pubSecKey);
    return JWTAuthHandler.create(JWTAuth.create(vertx, jwtAuthOptions));
  }
}
