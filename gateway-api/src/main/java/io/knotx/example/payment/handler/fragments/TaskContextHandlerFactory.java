package io.knotx.example.payment.handler.fragments;

import io.knotx.fragment.Fragment;
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

public class TaskContextHandlerFactory implements RoutingHandlerFactory {

  private static final String DEFAULT_TASK_KEY = "data-knotx-task";
  private static final String NO_BODY = "";

  @Override
  public String getName() {
    return "taskContextHandler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return routingContext -> handle(routingContext, config, getRequestContextEngine());
  }

  private void handle(RoutingContext routingContext, JsonObject config, RequestContextEngine requestContextEngine){
    RequestContext requestContext = routingContext.get(RequestContext.KEY);

    requestContextEngine
        .processAndSaveResult(toHandlerResult(requestContext, config), routingContext,
            requestContext);
  }

  private DefaultRequestContextEngine getRequestContextEngine() {
    return new DefaultRequestContextEngine(
        getClass().getSimpleName());
  }

  private RequestEventHandlerResult toHandlerResult(RequestContext requestContext,
      JsonObject config) {
    RequestEvent requestEvent = requestContext.getRequestEvent();
    requestEvent.getFragments().add(initStubFragment(config));
    return RequestEventHandlerResult.success(requestEvent);
  }

  private Fragment initStubFragment(JsonObject config) {
    String taskKey = config.getString("taskKey", DEFAULT_TASK_KEY);
    String taskName = config.getString("taskName");

    return new Fragment("stub", new JsonObject().put(taskKey, taskName), NO_BODY);
  }

}
