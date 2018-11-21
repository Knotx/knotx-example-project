/*
 * Copyright (C) 2018 Knot.x Project
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
package com.acme.gateway;

import io.knotx.server.handler.api.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.nio.charset.StandardCharsets;

public class AcmeGatewayHandlerFactory implements RoutingHandlerFactory {

  private final static String RESPONSE = "{\"message\":\"This is a acme endpoint response\"}";

  @Override
  public String getName() {
    return "acmeEndpointHandler";
  }

  @Override
  public Handler<RoutingContext> create(
      io.vertx.reactivex.core.Vertx vertx, JsonObject config) {
    return routingContext -> routingContext.response().end(RESPONSE, StandardCharsets.UTF_8.name());
  }
}
