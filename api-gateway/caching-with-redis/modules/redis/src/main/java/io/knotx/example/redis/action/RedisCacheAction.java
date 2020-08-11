package io.knotx.example.redis.action;

import io.knotx.example.redis.client.RedisConnectionManager;
import io.knotx.example.redis.util.JsonSerializer;
import io.knotx.fragments.action.api.Action;
import io.knotx.fragments.api.Fragment;
import io.knotx.fragments.api.FragmentContext;
import io.knotx.fragments.api.FragmentResult;
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
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Response;
import java.util.Optional;

/**
 * @see RedisCacheActionFactory
 */
public class RedisCacheAction implements Action {
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAction.class);

  private final RedisConnectionManager redisConnection;
  private final RedisCacheActionOptions options;
  private final Action doAction;

  RedisCacheAction(Vertx vertx, JsonObject config, Action doAction) {
    this.options = new RedisCacheActionOptions(config);
    this.doAction = doAction;

    redisConnection = new RedisConnectionManager(vertx, buildRedisOptions(), 2);
  }

  @Override
  public void apply(FragmentContext fragmentContext, Handler<AsyncResult<FragmentResult>> resultHandler) {
    String cacheKey = resolveCacheKey(fragmentContext.getClientRequest());

    LOGGER.info("Retrieving data from redis (key: {})", cacheKey);
    redisConnection.call(redisAPI -> redisAPI.get(cacheKey, response -> {
      Optional<Object> cachedValue = getObjectFromResponse(response.result());

      if (!cachedValue.isPresent()) {
        LOGGER.info("No valid cache for key '{}'. Calling the action", cacheKey);
        callDoActionAndCache(fragmentContext, resultHandler, cacheKey);
      } else {
        LOGGER.info("Retrieved value from cache under key '{}'", cacheKey);
        proceedWithCachedValue(fragmentContext, resultHandler, cachedValue.get());
      }
    }), () -> {
      LOGGER.warn("Connection with Redis isn't live, calling the action");
      callDoActionAndCache(fragmentContext, resultHandler, cacheKey);
    });
  }

  private String resolveCacheKey(ClientRequest clientRequest) {
    return PlaceholdersResolver.resolve(options.getCacheKey(), buildSourceDefinitions(clientRequest));
  }

  private SourceDefinitions buildSourceDefinitions(ClientRequest clientRequest) {
    return SourceDefinitions.builder()
        .addClientRequestSource(clientRequest)
        .build();
  }

  private RedisOptions buildRedisOptions() {
    SocketAddress address = SocketAddress.inetSocketAddress(options.getRedisPort(), options.getRedisHost());

    return new RedisOptions()
          .setEndpoint(address)
          .setPassword(options.getRedisPassword());
  }

  private void proceedWithCachedValue(FragmentContext fragmentContext, Handler<AsyncResult<FragmentResult>> resultHandler, Object cachedValue) {
    Fragment fragment = fragmentContext.getFragment();
    fragment.appendPayload(options.getPayloadKey(), cachedValue);

    FragmentResult result = new FragmentResult(fragment, FragmentResult.SUCCESS_TRANSITION);
    Future.succeededFuture(result).setHandler(resultHandler);
  }

  private void callDoActionAndCache(FragmentContext fragmentContext, Handler<AsyncResult<FragmentResult>> resultHandler, String cacheKey) {
    doAction.apply(fragmentContext, asyncResult -> {
      if (asyncResult.succeeded()) {
        FragmentResult fragmentResult = asyncResult.result();

        if (FragmentResult.SUCCESS_TRANSITION.equals(fragmentResult.getTransition())) {
          JsonObject resultPayload = fragmentResult.getFragment().getPayload();
          Object objectToSerialize = resultPayload.getMap().get(options.getPayloadKey());

          JsonSerializer
              .serializeObject(objectToSerialize)
              .ifPresent(serializedObject -> cacheObject(serializedObject, cacheKey));
        }

        Future.succeededFuture(fragmentResult)
            .setHandler(resultHandler);
      } else {
        Future.<FragmentResult>failedFuture(asyncResult.cause())
            .setHandler(resultHandler);
      }
    });
  }

  private Optional<Object> getObjectFromResponse(Response response) {
    return Optional.ofNullable(response)
        .map(Response::toString)
        .flatMap(JsonSerializer::deserializeObject);
  }

  private void cacheObject(String serializedObject, String cacheKey) {
    redisConnection.call(redisAPI -> {
      redisAPI.setex(cacheKey, options.getTtl(), serializedObject, response -> {
        if (response.succeeded()) {
          LOGGER.info("New value cached under key: {} for {} seconds", cacheKey, options.getTtl());
        } else {
          LOGGER.error("Error while caching new value under key: {}", response.cause(), cacheKey);
        }
      });
    }, () -> LOGGER.warn("Cannot cache a new value (key: '{}'), because the Redis connection isn't live", cacheKey));
  }
}
