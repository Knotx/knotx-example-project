package io.knotx.example.api.payment;

import io.reactivex.Single;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.circuitbreaker.impl.CircuitBreakerImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.circuitbreaker.CircuitBreaker;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaymentRxHandlerWithCircuitBreaker extends PaymentRxHandler implements
    Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRxHandlerWithCircuitBreaker.class);
  private static final long NO_TIMEOUT = -1;
  private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

  PaymentRxHandlerWithCircuitBreaker(Vertx vertx,
      JsonObject config) {
    super(vertx, config);
    circuitBreakerMap.put("user", initCircuitBreaker(vertx, config, "user"));
    circuitBreakerMap.put("paypal", initCircuitBreaker(vertx, config, "paypal"));
    circuitBreakerMap.put("payU", initCircuitBreaker(vertx, config, "payU"));
    circuitBreakerMap.put("creditCard", initCircuitBreaker(vertx, config, "creditCard"));
  }

  @Override
  protected Single<JsonObject> scheduleCall(WebClient webClient, JsonObject config,
      String service) {
    CircuitBreaker serviceCircuitBreaker = circuitBreakerMap.get(service);
    if (serviceCircuitBreaker != null) {
      JsonObject serviceData = config.getJsonObject(service);
      Integer port = serviceData.getInteger("port", 8080);
      String host = serviceData.getString("host", "webapi");
      String requestURI = serviceData.getString("requestUri");

      return serviceCircuitBreaker
          .rxExecuteCommandWithFallback(future ->
                  webClient.get(port, host, requestURI)
                      .timeout(serviceData.getLong("timeout", NO_TIMEOUT))
                      .rxSend()
                      .map(HttpResponse::bodyAsJsonObject)
                      .doOnSuccess(future::complete)
                      .doOnError(future::fail)
                      .doOnSuccess(response -> traceSuccessResponse(service, response))
                      .doOnError(error -> traceErrorResponse(service, error)),
              fallback -> {
                JsonObject result = serviceData.getJsonObject("fallback");
                LOGGER.warn("Service [{}] fallback [{}] applied!", service, result);
                return result;
              });
    } else {
      return super.scheduleCall(webClient, config, service);
    }
  }

  private CircuitBreaker initCircuitBreaker(Vertx vertx, JsonObject config,
      String serviceName) {
    Optional<CircuitBreakerOptions> circuitBreakerOptions = getCircuitBreakerOptions(config,
        serviceName);
    return circuitBreakerOptions.map(options -> CircuitBreaker.newInstance(
        new CircuitBreakerImpl(serviceName + "CircuitBreaker", vertx.getDelegate(), options)))
        .orElse(null);
  }

  private Optional<CircuitBreakerOptions> getCircuitBreakerOptions(JsonObject config,
      String serviceName) {
    JsonObject json = config.getJsonObject(serviceName).getJsonObject("circuitBreakerOptions");
    return json == null ? Optional.empty() : Optional.of(new CircuitBreakerOptions(json));
  }
}
