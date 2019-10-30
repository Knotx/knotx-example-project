package io.knotx.example.redis.action;

import io.knotx.example.redis.util.JsonSerializer;
import io.knotx.fragments.api.Fragment;
import io.knotx.fragments.handler.api.Action;
import io.knotx.fragments.handler.api.domain.FragmentContext;
import io.knotx.fragments.handler.api.domain.FragmentResult;
import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.common.placeholders.PlaceholdersResolver;
import io.knotx.server.common.placeholders.SourceDefinitions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.SocketAddress;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Response;
import java.util.Optional;

/**
 * @see RedisCacheActionFactory
 */
public class RedisCacheAction implements Action {
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAction.class);

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 6379;
  private static final long DEFAULT_TTL = 60;

  private final Redis redisClient;
  private final RedisAPI redis;
  private final JsonObject config;
  private final String payloadKey;
  private final long ttl;
  private final Action doAction;

  RedisCacheAction(Vertx vertx, JsonObject config, Action doAction) {
    this.config = config;
    this.doAction = doAction;
    this.payloadKey = getPayloadKey();

    JsonObject cacheConfig = config.getJsonObject("cache");
    ttl = cacheConfig != null
        ? cacheConfig.getLong("ttl", DEFAULT_TTL)
        : DEFAULT_TTL;

    redisClient = Redis.createClient(vertx, getRedisOptions(config));
    redis = RedisAPI.api(redisClient);
  }

  @Override
  public void apply(FragmentContext fragmentContext, Handler<AsyncResult<FragmentResult>> resultHandler) {
    String cacheKey = getCacheKey(fragmentContext.getClientRequest());

    redisClient.connect(onConnect -> {
      if (onConnect.succeeded()) {
        redis.get(cacheKey, response -> {
          Object cachedValue = getObjectFromResponse(response.result());

          if (cachedValue == null) {
            LOGGER.info("No valid cache for key: {}, calling the action", cacheKey);
            callDoActionAndCache(fragmentContext, resultHandler, cacheKey);
          } else {
            LOGGER.info("Retrieved value from cache under key {}", cacheKey);
            Fragment fragment = fragmentContext.getFragment();
            fragment.appendPayload(payloadKey, cachedValue);
            FragmentResult result = new FragmentResult(fragment,
                FragmentResult.SUCCESS_TRANSITION);
            Future.succeededFuture(result)
                .setHandler(resultHandler);
          }
        });
      } else {
        LOGGER.error("Couldn't connect to Redis, calling the action. Cause: {}", onConnect.cause());
        callDoActionAndCache(fragmentContext, resultHandler, cacheKey);
      }
    });
  }

  private String getPayloadKey() {
    return Optional.ofNullable(config.getString("payloadKey"))
        .orElseThrow(() -> new IllegalArgumentException(
            "Action requires payloadKey value in configuration."));
  }

  private String getCacheKey(ClientRequest clientRequest) {
    return Optional.ofNullable(config.getString("cacheKey"))
        .map(value -> PlaceholdersResolver.resolve(value, buildSourceDefinitions(clientRequest)))
        .orElseThrow(
            () -> new IllegalArgumentException("Action requires cacheKey value in configuration"));
  }

  private SourceDefinitions buildSourceDefinitions(ClientRequest clientRequest) {
    return SourceDefinitions.builder()
        .addClientRequestSource(clientRequest)
        .build();
  }

  private RedisOptions getRedisOptions(JsonObject config) {
    return Optional.ofNullable(config.getJsonObject("redis"))
        .map(redisConfig -> {
          String host = redisConfig.getString("host", DEFAULT_HOST);
          int port = redisConfig.getInteger("port", DEFAULT_PORT);
          String password = redisConfig.getString("password", null);

          return new RedisOptions()
            .setEndpoint(SocketAddress.inetSocketAddress(port, host))
            .setPassword(password);
        })
        .orElseGet(RedisOptions::new);
  }

  private void callDoActionAndCache(FragmentContext fragmentContext, Handler<AsyncResult<FragmentResult>> resultHandler, String cacheKey) {
    doAction.apply(fragmentContext, asyncResult -> {
      if (asyncResult.succeeded()) {
        FragmentResult fragmentResult = asyncResult.result();

        if (FragmentResult.SUCCESS_TRANSITION.equals(fragmentResult.getTransition())) {
          JsonObject resultPayload = fragmentResult.getFragment().getPayload();
          Object objectToSerialize = resultPayload.getMap().get(payloadKey);

          String serializedObject = JsonSerializer.serializeObject(objectToSerialize);
          cacheObject(serializedObject, cacheKey);
        }

        Future.succeededFuture(fragmentResult)
            .setHandler(resultHandler);
      } else {
        Future.<FragmentResult>failedFuture(asyncResult.cause())
            .setHandler(resultHandler);
      }
    });
  }

  private Object getObjectFromResponse(Response response) {
    return Optional.ofNullable(response)
        .map(Response::toString)
        .map(JsonSerializer::deserializeObject)
        .orElse(null);
  }

  private void cacheObject(String serializedObject, String cacheKey) {
    if (serializedObject != null) {
      redis.setex(cacheKey, Long.toString(ttl), serializedObject, response -> {
        if (response.succeeded()) {
          LOGGER.info("New value cached under key: {} for {} seconds", cacheKey, ttl);
        } else {
          LOGGER.error("Error while caching new value under key: {}", response.cause(), cacheKey);
        }
      });
    }
  }
}
