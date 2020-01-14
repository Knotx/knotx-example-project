package io.knotx.example.swagger.handler;

import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Primitive handler that returns either hardcoded JSON or XML based on "datatype" query parameter.
 * It has no real applications and is provided only as a mock implementation for demonstration purposes.
 */
public class AdminInfoHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "admin-info";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return routingContext -> {
      RequestContext requestContext = routingContext.get(RequestContext.KEY);
      ClientRequest clientRequest = requestContext.getRequestEvent().getClientRequest();

      String type = clientRequest.getParams().get("datatype");
      respondByType(routingContext.response(), type);
    };
  }

  private void respondByType(HttpServerResponse response, String type) {
    String contentType = "text/plain";
    String content = "";

    if ("json".equals(type)) {
      contentType = "application/json";
      content = "{\"admin\": false}";
    } else if ("xml".equals(type)) {
      contentType = "application/xml";
      content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><IsAdmin><admin>false</admin></IsAdmin>";
    }

    response
        .putHeader("Content-Type", contentType)
        .end(content);
  }
}
