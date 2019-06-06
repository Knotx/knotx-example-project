package io.knotx.example.payment.handler.fragments;

import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.context.RequestEvent;
import io.knotx.server.api.handler.DefaultRequestContextEngine;
import io.knotx.server.api.handler.RequestContextEngine;
import io.knotx.server.api.handler.RequestEventHandlerResult;
import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class PayloadToBodyHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "payloadToBodyHandler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {

    RequestContextEngine requestContextEngine = new DefaultRequestContextEngine(
        getClass().getSimpleName());

    return routingContext -> {
      RequestContext requestContext = routingContext.get(RequestContext.KEY);
      requestContextEngine
          .processAndSaveResult(toHandlerResult(requestContext), routingContext,
              requestContext);
    };
  }

  private RequestEventHandlerResult toHandlerResult(RequestContext requestContext) {
    RequestEvent requestEvent = requestContext.getRequestEvent();
    requestEvent.getFragments()
        .forEach(fragment -> fragment.setBody(fragment.getPayload().encodePrettily()));

    return RequestEventHandlerResult.success(requestEvent);
  }

}
