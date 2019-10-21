package io.knotx.example.healthcheck;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.healthchecks.HealthCheckHandler;
import io.vertx.reactivex.ext.healthchecks.HealthChecks;
import io.vertx.reactivex.ext.web.RoutingContext;

public class HealthCheckHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "healthcheck";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    HealthChecks checks = HealthChecks.create(vertx);
    checks.register("dummy check", 200, future -> {
      // do check here

      future.complete(Status.OK());
    });
    return HealthCheckHandler.createWithHealthChecks(checks);
  }
}