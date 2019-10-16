package io.knotx.example.books.action;

import io.knotx.fragments.handler.api.Action;
import io.knotx.fragments.handler.api.ActionFactory;
import io.knotx.fragments.handler.api.domain.FragmentResult;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ExposePayloadActionFactory implements ActionFactory {

  @Override
  public String getName() {
    return "expose-payload-data";
  }

  @Override
  public Action create(String alias, JsonObject config, Vertx vertx, Action doAction) {
    String key = config.getString("key");
    String exposeAs = config.getString("exposeAs");

    return (fragmentContext, resultHandler) ->
        Single.just(fragmentContext.getFragment())
          .map(fragment -> {
            JsonObject exposedData = fragment.getPayload().getJsonObject(key).getJsonObject("_result");
            fragment.appendPayload(exposeAs, exposedData);
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
}
