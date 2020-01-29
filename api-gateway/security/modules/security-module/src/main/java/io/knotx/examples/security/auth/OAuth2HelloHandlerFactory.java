package io.knotx.examples.security.auth;

import io.knotx.server.api.security.AuthHandlerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.oauth2.OAuth2Auth;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.AuthHandler;
import io.vertx.reactivex.ext.web.handler.OAuth2AuthHandler;

public class OAuth2HelloHandlerFactory implements AuthHandlerFactory {

  private OAuth2AuthHandler oAuth2AuthHandler;

  @Override
  public String getName() {
    return "helloOAuth2AuthFactory";
  }

  @Override
  public AuthHandler create(Vertx vertx, JsonObject config) {
    OAuth2ClientOptions clientOptions = new OAuth2ClientOptions()
        .setClientID(config.getString("clientId"))
        .setClientSecret(config.getString("clientSecret"))
        .setSite("https://accounts.google.com")
        .setTokenPath("https://www.googleapis.com/oauth2/v4/token")
        .setAuthorizationPath("/o/oauth2/auth")
        .setUserInfoPath("https://www.googleapis.com/oauth2/v3/userinfo")
        .setIntrospectionPath("https://www.googleapis.com/oauth2/v3/tokeninfo")
        .setUserInfoParameters(new JsonObject().put("alt", "json"))
        .setScopeSeparator(" ")
        .setUseBasicAuthorizationHeader(false);

    OAuth2Auth authProvider = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, clientOptions);

    oAuth2AuthHandler = OAuth2AuthHandler.create(authProvider, config.getString("redirectUrl"));
    config.getJsonArray("scopes").forEach(scope -> oAuth2AuthHandler.addAuthority((String) scope));

    return oAuth2AuthHandler;
  }

  @Override
  public void registerCustomRoute(Router router) {
    oAuth2AuthHandler.setupCallback(router.route());
  }
}
