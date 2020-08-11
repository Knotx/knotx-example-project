package io.knotx.example.redis.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Optional;

public class JsonSerializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

  private JsonSerializer() {
    // util
  }

  public static Optional<String> serializeObject(Object object) {
    if (object instanceof JsonObject || object instanceof JsonArray) {
      return Optional.of(object.toString());
    } else {
      LOGGER.error("Couldn't serialize object {}, it's neither JsonObject nor JsonArray", object);
      return Optional.empty();
    }
  }

  public static Optional<Object> deserializeObject(String json) {
    if (json.startsWith("{")) {
      return Optional.of(new JsonObject(json));
    } else if (json.startsWith("[")) {
      return Optional.of(new JsonArray(json));
    } else {
      LOGGER.error("Couldn't deserialize string, it doesn't look like a correct JSON: {}", json);
      return Optional.empty();
    }
  }
}
