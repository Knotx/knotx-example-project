package io.knotx.example.redis.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class JsonSerializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

  private JsonSerializer() {
    // util
  }

  public static String serializeObject(Object object) {
    if (object instanceof JsonObject) {
      return ((JsonObject) object).encode();
    } else if (object instanceof JsonArray) {
      return ((JsonArray) object).encode();
    } else {
      LOGGER.error("Couldn't serialize object {}, it's neither JsonObject nor JsonArray", object);
      return null;
    }
  }

  public static Object deserializeObject(String json) {
    if (json.startsWith("{")) {
      return new JsonObject(json);
    } else if (json.startsWith("[")) {
      return new JsonArray(json);
    } else {
      LOGGER.error("Couldn't deserialize string, it doesn't look like a correct JSON: {}", json);
      return null;
    }
  }
}
