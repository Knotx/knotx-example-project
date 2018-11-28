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
package com.acme.gateway.sockjs;

import io.knotx.server.handler.api.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

public class AcmeSockJsGatewayHandlerFactory implements RoutingHandlerFactory {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AcmeSockJsGatewayHandlerFactory.class);

  @Override
  public String getName() {
    return "acmeSockJsEndpointHandler";
  }

  @Override
  public Handler<RoutingContext> create(
      io.vertx.reactivex.core.Vertx vertx, JsonObject config) {

    BridgeOptions options = new BridgeOptions()
        .addOutboundPermitted(new PermittedOptions().setAddress(config.getString("feed")));

    return SockJSHandler.create(vertx).bridge(options, event -> {
      if (event.type() == BridgeEventType.SOCKET_CREATED) {
        LOGGER.info("The comments socket was created");
      }
      event.complete(true);
    });
  }
}
