package io.knotx.example.books.action;

import io.knotx.fragments.action.api.Action;
import io.knotx.fragments.action.api.ActionFactory;
import io.knotx.fragments.api.FragmentResult;

import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Reads data from payload under "key" key and puts it under "exposeAs" key.
 * Key names can be defined in configuration like:
 * <pre>
 *   config {
 *      key = key1
*       exposeAs = key2
*    }
 * </pre>
 */
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
        .subscribe(
            onSuccess -> complete(resultHandler, Future.succeededFuture(onSuccess)),
            onError -> complete(resultHandler, Future.failedFuture(onError))
        );
  }

  private void complete(Handler<AsyncResult<FragmentResult>> resultHandler, Future<FragmentResult> fragmentResultFuture) {
    fragmentResultFuture.setHandler(resultHandler);
  }
}
