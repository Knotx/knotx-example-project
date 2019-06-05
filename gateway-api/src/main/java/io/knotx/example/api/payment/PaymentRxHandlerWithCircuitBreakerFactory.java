package io.knotx.example.api.payment;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class PaymentRxHandlerWithCircuitBreakerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "paymentRxHandlerWithCircuitBreaker";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return new PaymentRxHandlerWithCircuitBreaker(vertx, config);
  }

}
