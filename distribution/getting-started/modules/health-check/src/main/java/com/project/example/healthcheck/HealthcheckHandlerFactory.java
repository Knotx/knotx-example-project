package com.project.example.healthcheck;

import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.healthchecks.HealthCheckHandler;
import io.vertx.reactivex.ext.healthchecks.HealthChecks;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.WebClient;

public class HealthcheckHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "healthcheck";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    HealthChecks checks = HealthChecks.create(vertx);
    checks.register("API check", 200, future -> {
      WebClient webClient = WebClient.create(vertx);
      webClient.get(8092, "localhost", "/api/hello")
          .rxSend()
          .subscribe(onSuccess -> {
            JsonObject jsonResponse = onSuccess.bodyAsJsonObject();
            future.complete("Hello World From Knot.x!".equals(jsonResponse.getString("message")) ? Status.OK() :
                Status.KO());
          }, onError -> future
              .complete(Status.KO(new JsonObject().put("error", onError.getMessage()))));
    });
    return HealthCheckHandler.createWithHealthChecks(checks);
  }
}
