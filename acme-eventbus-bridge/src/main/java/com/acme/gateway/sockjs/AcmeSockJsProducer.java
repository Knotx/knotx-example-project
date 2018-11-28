package com.acme.gateway.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class AcmeSockJsProducer extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcmeSockJsProducer.class);

  private String commentsFeedName;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    commentsFeedName = this.config().getString("feed", "comments-feed");
  }

  @Override
  public void start() {
    vertx.setPeriodic(1000, t -> {
      LOGGER.info("Sending message to feed [{}]", commentsFeedName);
      vertx.eventBus()
          .publish(commentsFeedName, System.currentTimeMillis() + "comments from the server!");
    });
  }
}
