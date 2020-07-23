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

import static io.knotx.example.payment.utils.ProvidersProvider.calculateProviders;

import org.apache.commons.lang3.StringUtils;

import io.knotx.fragments.action.api.Action;
import io.knotx.fragments.action.api.ActionFactory;
import io.knotx.fragments.api.FragmentResult;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class PaymentsActionFactory implements ActionFactory {

  @Override
  public String getName() {
    return "payments";
  }

  @Override
  public Action create(String alias, JsonObject config, Vertx vertx, Action doAction) {
    return (fragmentContext, resultHandler) ->
        Single.just(fragmentContext.getFragment())
            .map(fragment -> {
              JsonObject payload = fragment.getPayload();
              JsonObject user = payload.getJsonObject("user");
              JsonObject payments = processProviders(payload);
              fragment.clearPayload();
              fragment.mergeInPayload(new JsonObject().put(getAlias(alias), payments)
                  .put("user", user));
              return new FragmentResult(fragment, FragmentResult.SUCCESS_TRANSITION);
            })
            .subscribe(onSuccess -> {
              Future<FragmentResult> resultFuture = Future.succeededFuture(onSuccess);
              resultFuture.setHandler(resultHandler);
            }, onError -> {
              Future<FragmentResult> resultFuture = Future.failedFuture(onError);
              resultFuture.setHandler(resultHandler);
            });
  }

  private JsonObject processProviders(JsonObject payload) {
    return new JsonObject()
        .put("timestamp", System.currentTimeMillis())
        .put("providers", calculateProviders(payload));
  }

  private String getAlias(String alias) {
    return StringUtils.defaultString(alias, "payments");
  }
}
