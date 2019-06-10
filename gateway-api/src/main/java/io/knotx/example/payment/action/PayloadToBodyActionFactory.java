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
package io.knotx.example.payment.action;

import static io.vertx.core.Future.succeededFuture;

import java.util.Objects;
import java.util.Optional;

import io.knotx.fragment.Fragment;
import io.knotx.fragments.handler.api.Action;
import io.knotx.fragments.handler.api.ActionFactory;
import io.knotx.fragments.handler.api.domain.FragmentResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class PayloadToBodyActionFactory implements ActionFactory {
  private static final String KEY = "key";

  @Override
  public String getName() {
    return "payload-to-body";
  }

  @Override
  public Action create(String alias, JsonObject config, Vertx vertx, Action doAction) {
    checkArgument(doAction != null, "Payload to body action does not support doAction");

    return (fragmentContext, resultHandler) -> {
      Fragment fragment = fragmentContext.getFragment();
      String payloadKey = Objects.nonNull(config) ? config.getString(KEY) : null;

      FragmentResult result = getBodyFromPayload(payloadKey, fragment.getPayload())
        .map(body -> toFragmentResult(fragment, body))
        .orElse(new FragmentResult(fragment, FragmentResult.ERROR_TRANSITION));

      Future<FragmentResult> resultFuture = succeededFuture(result);
      resultFuture.setHandler(resultHandler);
    };
  }

  private FragmentResult toFragmentResult(Fragment fragment, String body) {
    fragment.setBody(body);
    return new FragmentResult(fragment, FragmentResult.SUCCESS_TRANSITION);
  }

  private Optional<String> getBodyFromPayload(String key, JsonObject payload){
    JsonObject body = Objects.isNull(key) ? payload : payload.getJsonObject(key);
    if (Objects.isNull(body)){
      return Optional.empty();
    }

    return Optional.of(body.encodePrettily());
  }

  private void checkArgument(boolean condition, String message){
    if(condition){
      throw new IllegalArgumentException(message);
    }
  }
}