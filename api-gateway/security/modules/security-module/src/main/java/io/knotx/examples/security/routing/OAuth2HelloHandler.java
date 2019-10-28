package io.knotx.examples.security.routing;

import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.apache.commons.lang3.StringUtils;

public class OAuth2HelloHandler implements Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2HelloHandler.class);

  private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

  private static final String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";

  private WebClient webClient;

  private JsonObject config;

  OAuth2HelloHandler(Vertx vertx, JsonObject config) {
    webClient = WebClient.create(vertx);
    this.config = config;
  }

  @Override
  public void handle(RoutingContext event) {
    String code = event.request().getParam("code");

    exchangeCodeForToken(code)
        .flatMap(this::fetchUserInfo)
        .subscribe(response -> {
          LOGGER.info("Response from Google userinfo endpoint: {}", response.statusCode());

          JsonObject body = response.bodyAsJsonObject();
          String name = body.getString("name");

          event.response().end("Hello " + name);
        }, error -> LOGGER.error("An error occurred: {}", error));
  }

  private Single<String> exchangeCodeForToken(String code) {
    return prepareTokenRequest(code, config)
        .rxSend()
        .map(response -> {
          LOGGER.info("Response from Google token endpoint: {}", response.statusCode());
          JsonObject body = response.bodyAsJsonObject();
          return body.getString("access_token");
        });
  }

  private HttpRequest<Buffer> prepareTokenRequest(String code, JsonObject config) {
    String clientId = config.getString("clientId");
    String clientSecret = config.getString("clientSecret");
    String redirectUri = config.getString("redirectUri");

    if (StringUtils.isAnyBlank(code, clientId, clientSecret, redirectUri)) {
      throw new IllegalArgumentException("Configuration for Google Auth must include code, clientId, clientSecret and redirectUri");
    }

    return webClient.postAbs(TOKEN_URL)
        .setQueryParam("code", code)
        .setQueryParam("client_id", clientId)
        .setQueryParam("client_secret", clientSecret)
        .setQueryParam("redirect_uri", redirectUri)
        .setQueryParam("grant_type", "authorization_code")
        .putHeader("Content-Length", "0");
  }

  private Single<HttpResponse<Buffer>> fetchUserInfo(String accessToken) {
    return webClient.getAbs(USERINFO_URL)
        .bearerTokenAuthentication(accessToken)
        .rxSend();
  }

}
