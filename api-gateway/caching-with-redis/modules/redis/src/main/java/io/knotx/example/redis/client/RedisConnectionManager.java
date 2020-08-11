package io.knotx.example.redis.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Redis;

import java.util.function.Consumer;

public class RedisConnectionManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisConnectionManager.class);

  private final Vertx vertx;
  private final RedisOptions options;
  private final int maxReconnectRetries;

  private Redis client;
  private RedisAPI redisApi;
  private boolean connected;

  public RedisConnectionManager(Vertx vertx, RedisOptions options, int maxReconnectRetries) {
    this.vertx = vertx;
    this.options = options;
    this.maxReconnectRetries = maxReconnectRetries;

    connect(onConnect -> {});
  }

  public void call(Consumer<RedisAPI> ifConnected, Runnable ifNotConnected) {
    if (connected) {
      ifConnected.accept(redisApi);
    } else {
      ifNotConnected.run();
    }
  }

  private void connect(Handler<AsyncResult<Redis>> handler) {
    Redis.createClient(vertx, options).connect(onConnect -> {
      if (onConnect.succeeded()) {
        client = onConnect.result();
        redisApi = RedisAPI.api(client);
        connected = true;

        client.exceptionHandler(this::reconnect);
      } else {
        connected = false;
      }

        handler.handle(onConnect);
    });
  }

  private long getReconnectDelay(int retry) {
    long retryFactor = Math.min(retry, 10);
    return (long) Math.pow(2, retryFactor * 10);
  }

  private void reconnect(Throwable e) {
    LOGGER.error("Connection to Redis lost. Cause: {}", e);
    retryReconnect(0);
  }

  private void retryReconnect(int retry) {
    long delay = getReconnectDelay(retry);

    vertx.setTimer(delay, timer -> connect(onReconnect -> {
      if (onReconnect.failed()) {
        if (retry + 1 > maxReconnectRetries) {
          LOGGER.error("Couldn't connect to Redis (tried {} reconnections). Cause: {}", retry, onReconnect.cause());
        } else {
          retryReconnect(retry + 1);
        }
      }
    }));
  }
}
