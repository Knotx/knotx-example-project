package io.knotx.example.action.payments;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public final class ProvidersProvider { // lol :D

  private ProvidersProvider() {
    //util
  }

  public static JsonArray calculateProviders(JsonObject creditCard,
      JsonObject paypal, JsonObject payU) {
    JsonArray providers = new JsonArray();
    if (creditCard != null && creditCard.containsKey("allowed") && creditCard
        .getBoolean("allowed")) {
      providers.add(getProviderData(creditCard, "label", "url"));
    }
    if (paypal != null && paypal.containsKey("verified") && paypal.getBoolean("verified")) {
      providers.add(getProviderData(paypal, "label", "paymentUrl"));
    }
    if (payU != null && "OK".equals(payU.getString("status"))) {
      providers.add(getProviderData(payU, "name", "link"));
    }
    return providers;
  }

  public static JsonArray calculateProviders(JsonObject payload) {
    return calculateProviders(getResult(payload, "creditCard"), getResult(payload, "paypal"),
        getResult(payload, "payU"));
  }


  private static JsonObject getProviderData(JsonObject data, String label, String paymentUrl) {
    return new JsonObject()
        .put("label", data.getString(label))
        .put("paymentUrl", data.getString(paymentUrl));
  }

  private static JsonObject getResult(JsonObject payload, String provider) {
    if (payload.containsKey(provider)) {
      return payload.getJsonObject(provider).getJsonObject("_result");
    } else {
      return null;
    }
  }

}
