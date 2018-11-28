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
package com.acme.socket.feed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class AcmeCommentsEventBusFeed extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcmeCommentsEventBusFeed.class);

  private String feed;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    feed = this.config().getString("feed");
  }

  @Override
  public void start() {
    vertx.setPeriodic(5000, t -> {
      LOGGER.info("Sending message to feed [{}]", feed);
      vertx.eventBus()
          .publish(feed, System.currentTimeMillis() + " comment from the server!");
    });
  }
}
