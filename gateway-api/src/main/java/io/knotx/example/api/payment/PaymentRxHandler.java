package io.knotx.example.api.payment;

import static io.knotx.example.action.payments.ProvidersProvider.calculateProviders;

import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class PaymentRxHandler implements Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRxHandlerFactory.class);
  private static final long NO_TIMEOUT = -1;

  private JsonObject config;
  private final WebClient userWebClient;
  private final WebClient paypalWebClient;
  private final WebClient payUWebClient;
  private final WebClient creditCardWebClient;

  PaymentRxHandler(Vertx vertx, JsonObject config) {
    this.config = config;
    userWebClient = initWebClient(vertx, config, "user");
    paypalWebClient = initWebClient(vertx, config, "paypal");
    payUWebClient = initWebClient(vertx, config, "payU");
    creditCardWebClient = initWebClient(vertx, config, "creditCard");
  }

  @Override
  public void handle(RoutingContext event) {
    Single<JsonObject> userCall = scheduleCall(userWebClient, config, "user");
    Single<JsonObject> paypalCall = scheduleCall(paypalWebClient, config, "paypal");
    Single<JsonObject> payUCall = scheduleCall(payUWebClient, config, "payU");
    Single<JsonObject> creditCardCall = scheduleCall(creditCardWebClient, config, "creditCard");

    userCall.flatMap(userResponse ->
        Single.zip(paypalCall, payUCall, creditCardCall,
            (r1, r2, r3) -> mergeResponses(userResponse, r1, r2, r3))
            .map(this::toResult))
        .subscribe(merged -> {
          LOGGER.info("Payments API response [{}]", merged.encode());
          event.response().end(merged.encodePrettily());
        }, error -> {
          LOGGER.error("Payments API failed!", error);
          event.response().setStatusCode(500).end();
        });
  }

  private JsonObject toResult(JsonObject entries) {
    return new JsonObject()
        .put("timestamp", System.currentTimeMillis())
        .put("providers", calculateProviders(entries));
  }

  protected Single<JsonObject> scheduleCall(WebClient webClient, JsonObject config,
      String serviceAlias) {
    JsonObject serviceData = config.getJsonObject(serviceAlias);
    Integer port = serviceData.getInteger("port", 8080);
    String host = serviceData.getString("host", "webapi");
    String requestURI = serviceData.getString("requestUri");

    return Single.just(serviceAlias)
        .map(this::traceService)
        .flatMap(alias -> webClient
            .get(port, host, requestURI)
            .timeout(serviceData.getLong("timeout", NO_TIMEOUT))
            .rxSend()
        )
        .map(HttpResponse::bodyAsJsonObject)
        .doOnSuccess(response -> traceSuccessResponse(serviceAlias, response))
        .doOnError(error -> traceErrorResponse(serviceAlias, error));
  }

  protected String traceService(String alias) {
    LOGGER.trace("Calling service [{}]", alias);
    return alias;
  }

  protected void traceSuccessResponse(String serviceAlias, JsonObject response) {
    LOGGER.trace("Received service [{}] response [{}]", serviceAlias, response.encode());
  }

  protected void traceErrorResponse(String serviceAlias, Throwable error) {
    LOGGER.error("Service [{}] invocation failed!", serviceAlias, error);
  }

  private JsonObject mergeResponses(JsonObject userResponse, JsonObject paypalResponse,
      JsonObject payUResponse, JsonObject creditCardResponse) {
    return new JsonObject()
        .put("user", new JsonObject().put("_result", userResponse))
        .put("paypal", new JsonObject().put("_result", paypalResponse))
        .put("creditCard", new JsonObject().put("_result", creditCardResponse))
        .put("payU", new JsonObject().put("_result", payUResponse));
  }

  private WebClient initWebClient(Vertx vertx, JsonObject config, String serviceName) {
    return WebClient.create(vertx, getWebClientOptions(config, serviceName));
  }

  private WebClientOptions getWebClientOptions(JsonObject config, String serviceName) {
    JsonObject json = config.getJsonObject(serviceName).getJsonObject("webClientOptions");
    return json == null ? new WebClientOptions() : new WebClientOptions(json);
  }
}
