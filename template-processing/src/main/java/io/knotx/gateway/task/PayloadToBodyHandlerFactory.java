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

    RequestContextEngine requestContextEngine =
        new DefaultRequestContextEngine(getClass().getSimpleName());

    return routingContext -> {
      RequestContext requestContext = routingContext.get(RequestContext.KEY);
      requestContextEngine.processAndSaveResult(
          toHandlerResult(requestContext), routingContext, requestContext);
    };
  }

  private RequestEventHandlerResult toHandlerResult(RequestContext requestContext) {
    RequestEvent requestEvent = requestContext.getRequestEvent();
    requestEvent
        .getFragments()
        .forEach(fragment -> fragment.setBody(fragment.getPayload().encodePrettily()));

    return RequestEventHandlerResult.success(requestEvent);
  }
}
