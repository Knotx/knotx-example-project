package io.knotx.examples.security.routing;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.net.URISyntaxException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;


public class GoogleLoginRedirectHandlerFactory implements RoutingHandlerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleLoginRedirectHandlerFactory.class);

  private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";

  @Override
  public String getName() {
    return "google-login-redirect-handler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    try {
      String authorizationUri = getAuthorizationUri(config);

      return event -> event.response()
          .putHeader("Location", authorizationUri)
          .setStatusCode(HttpResponseStatus.SEE_OTHER.code())
          .end();
    } catch (URISyntaxException e) {
      LOGGER.error("Error while building the authorization URI: {}", e);
      return event -> event.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
    }
  }

  private static String getAuthorizationUri(JsonObject config) throws URISyntaxException {
    String redirectUri = config.getString("redirectUri");
    String scope = config.getString("scope");
    String clientId = config.getString("clientId");
    String state = RandomStringUtils.random(20);
    String nonce = RandomStringUtils.random(20);

    if (StringUtils.isAnyBlank(redirectUri, scope, clientId)) {
      throw new IllegalArgumentException("Configuration for Google Auth must include redirectUri, clientId and scope");
    }

    return new URIBuilder(AUTH_URL)
        .addParameter("redirect_uri", redirectUri)
        .addParameter("scope", scope)
        .addParameter("client_id", clientId)
        .addParameter("state", state)
        .addParameter("nonce", nonce)
        .addParameter("prompt", "consent")
        .addParameter("access_type", "offline")
        .addParameter("response_type", "code")
        .build().toString();
  }
}
