package io.knotx.example.payment.action;

import static io.vertx.core.Future.succeededFuture;

import java.util.Objects;

import io.knotx.fragments.handler.api.Action;
import io.knotx.fragments.handler.api.ActionFactory;
import io.knotx.fragments.handler.api.domain.FragmentContext;
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
      fragmentContext.getFragment()
          .setBody(getBodyFromPayload(config.getString(KEY), fragmentContext.getFragment().getPayload()));

      Future<FragmentResult> resultFuture = succeededFuture(getResult(fragmentContext));
      resultFuture.setHandler(resultHandler);
    };
  }

  private FragmentResult getResult(FragmentContext fragmentContext) {
    return new FragmentResult(fragmentContext.getFragment(), FragmentResult.SUCCESS_TRANSITION);
  }

  private String getBodyFromPayload(String key, JsonObject payload){
    JsonObject body = Objects.isNull(key) ? payload : payload.getJsonObject(key);
    checkState(Objects.isNull(body), String.format("Cannot find value in payload under key: %s", key));

    return body.encodePrettily();
  }

  private void checkArgument(boolean condition, String message){
    if(condition){
      throw new IllegalArgumentException(message);
    }
  }

  private void checkState(boolean condition, String message){
    if(condition){
      throw new IllegalStateException(message);
    }
  }
}