package io.knotx.example.redis.action;

import io.knotx.fragments.handler.api.Action;
import io.knotx.fragments.handler.api.ActionFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Action factory for caching fragment payload values on Redis server. Can be initialized with a configuration:
 * <pre>
 *   productDetails {
 *     factory = redis-cache
 *     config {
 *       redis {
 *         host = localhost
 *         port = 6379
 *         password = my-password
 *       }
 *       cache.ttl = 60
 *       cacheKey = product-{param.id}
 *       payloadKey = product
 *     }
 *     doAction = fetch-product
 *   }
 * </pre>
 *
 * Parameters:
 * <pre>
 *   redis.host - default value: "localhost"
 *   redis.port - default value: 6379
 *   redis.password - empty by default
 *   cache.ttl - in seconds, default value: 60
 * </pre>
 */
public class RedisCacheActionFactory implements ActionFactory {

  @Override
  public String getName() {
    return "redis-cache";
  }

  @Override
  public Action create(String alias, JsonObject config, Vertx vertx, Action doAction) {
    return new RedisCacheAction(vertx, config, doAction);
  }

}
