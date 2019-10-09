/*
 * Copyright (C) 2019 Knot.x Project
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
package io.knotx.example.payment.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public final class ProvidersProvider {

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
      return payload.getJsonObject(provider)
          .getJsonObject("_result");
    } else {
      return null;
    }
  }

}
