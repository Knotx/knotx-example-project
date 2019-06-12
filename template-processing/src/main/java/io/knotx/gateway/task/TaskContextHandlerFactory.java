/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.gateway.task;

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
    RequestContextEngine requestContextEngine =
        new DefaultRequestContextEngine(getClass().getSimpleName());

    return routingContext -> {
      RequestContext requestContext = routingContext.get(RequestContext.KEY);
      requestContextEngine.processAndSaveResult(
          toHandlerResult(requestContext, config), routingContext, requestContext);
    };
  }

  private RequestEventHandlerResult toHandlerResult(
      RequestContext requestContext, JsonObject config) {
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
