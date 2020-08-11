package io.knotx.example.redis.action;

import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class RedisCacheActionOptions {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 6379;
  private static final long DEFAULT_TTL = 60;

  private final long ttl;
  private final String payloadKey;
  private final String cacheKey;
  private final String redisHost;
  private final int redisPort;
  private final String redisPassword;

  RedisCacheActionOptions(JsonObject config) {
    ttl = Optional.of(config)
        .map(conf -> conf.getJsonObject("cache"))
        .map(cacheConf -> cacheConf.getLong("ttl", DEFAULT_TTL))
        .orElse(DEFAULT_TTL);

    redisHost = Optional.of(config)
        .map(conf -> conf.getJsonObject("redis"))
        .map(redisConf -> redisConf.getString("host"))
        .orElse(DEFAULT_HOST);

    redisPort = Optional.of(config)
        .map(conf -> conf.getJsonObject("redis"))
        .map(redisConf -> redisConf.getInteger("port"))
        .orElse(DEFAULT_PORT);

    redisPassword = Optional.of(config)
        .map(conf -> conf.getJsonObject("redis"))
        .map(redisConf -> redisConf.getString("password"))
        .orElse(null);

    payloadKey = Optional.of(config)
        .map(conf-> conf.getString("payloadKey"))
        .orElseThrow(() -> new IllegalArgumentException("Action requires payloadKey value in configuration."));

    cacheKey = Optional.of(config)
        .map(conf-> conf.getString("cacheKey"))
        .orElseThrow(() -> new IllegalArgumentException("Action requires cacheKey value in configuration."));
  }

  public String getTtl() {
    return Long.toString(ttl);
  }

  public String getPayloadKey() {
    return payloadKey;
  }

  public String getCacheKey() {
    return cacheKey;
  }

  public String getRedisHost() {
    return redisHost;
  }

  public int getRedisPort() {
    return redisPort;
  }

  public String getRedisPassword() {
    return redisPassword;
  }
}
