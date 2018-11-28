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
package com.acme.socket.server;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;
import java.util.List;
import java.util.stream.Collectors;

public class AcmeEventBusBridgeServer extends AbstractVerticle {

  private static final int DEFAULT_EVENTBUS_BRIDGE_SERVER_PORT = 8093;
  private static final String DEFAULT_EVENTBUS_PATH_PATTERN = "/eventbus/*";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AcmeEventBusBridgeServer.class);

  private int port;
  private String pathPattern;
  private List<String> feeds;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    port = context.config().getInteger("port", DEFAULT_EVENTBUS_BRIDGE_SERVER_PORT);
    pathPattern = context.config().getString("pathPattern", DEFAULT_EVENTBUS_PATH_PATTERN);
    feeds = context.config().getJsonArray("feeds").stream().map(Object::toString).collect(
        Collectors.toList());
  }

  @Override
  public void start(Future<Void> fut) {
    HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(port));
    httpServer
        .requestHandler(getRouter()::accept)
        .rxListen()
        .subscribe(ok -> {
              LOGGER.info("Socket server started. Listening on port {}", port);
              fut.complete();
            },
            error -> {
              LOGGER.error("Unable to start socket server.", error.getCause());
              fut.fail(error);
            }
        );
  }

  private Router getRouter() {
    BridgeOptions options = new BridgeOptions();
    feeds.forEach(feed -> options.addOutboundPermitted(new PermittedOptions().setAddress(feed)));

    Router router = Router.router(vertx);
    router.route(pathPattern)
        .handler(SockJSHandler.create(vertx).bridge(options, event -> {
          if (event.type() == BridgeEventType.SOCKET_CREATED) {
            LOGGER.info("A socket was created!");
          }
          event.complete(true);
        }));
    router.route().handler(StaticHandler.create());
    return router;
  }
}
