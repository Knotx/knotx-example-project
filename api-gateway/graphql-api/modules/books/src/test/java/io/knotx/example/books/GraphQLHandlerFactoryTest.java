package io.knotx.example.books;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.knotx.junit5.util.HoconLoader;
import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.RequestContext;
import io.knotx.server.api.context.RequestEvent;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.impl.ParsableHeaderValuesContainer;
import io.vertx.ext.web.impl.ParsableMIMEValue;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.net.URL;
import java.util.Collections;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class GraphQLHandlerFactoryTest {

  @Test
  void expectBook(Vertx vertx, VertxTestContext testContext) throws Throwable {
    GraphQLHandlerFactory factory = new GraphQLHandlerFactory();
    getConfig(conf -> {
      // given
      String query = "{\"query\": \"{"
          + "    book(id: \\\"q5NoDwAAQBAJ\\\") {"
          + "        title "
          + "        publisher "
          + "        authors "
          + "    }"
          + "}\"}";
      Handler<RoutingContext> tested = factory.create(vertx, conf);
      RoutingContext rxRoutingContext = mockRoutingContext(vertx, testContext, query);

      // when, then
      tested.handle(rxRoutingContext);
    }, vertx);
  }

  @Test
  void expectBooks(Vertx vertx, VertxTestContext testContext) throws Throwable {
    GraphQLHandlerFactory factory = new GraphQLHandlerFactory();
    getConfig(conf -> {
      // given
      String query = "{\"query\": \"{"
          + "    books(match: \\\"graphql\\\") {"
          + "        title "
          + "        publisher "
          + "        authors "
          + "    }"
          + "}\"}";
      Handler<RoutingContext> tested = factory.create(vertx, conf);
      RoutingContext rxRoutingContext = mockRoutingContext(vertx, testContext, query);

      // when, then
      tested.handle(rxRoutingContext);
    }, vertx);
  }

  private HttpServerResponse mockResponse(VertxTestContext testContext) {
    HttpServerResponse response = mock(HttpServerResponse.class);
    when(response.putHeader(eq(HttpHeaders.CONTENT_TYPE), eq("application/json")))
        .thenReturn(response);
    doAnswer(invocation -> {
      testContext.verify(() -> {
        String responseContent = invocation.getArgument(0).toString();
        assertTrue(responseContent.contains("Learning GraphQL"));
      });
      testContext.completeNow();
      return null;
    }).when(response).end(any(Buffer.class));
    return response;
  }

  private void getConfig(Consumer<JsonObject> consumer, Vertx vertx) throws Throwable {
    URL resource = GraphQLHandlerFactoryTest.class
        .getResource("/routes/handlers/graphqlHandler.conf");
    HoconLoader.verify(resource.getPath(), consumer, vertx);
  }

  private RoutingContext mockRoutingContext(Vertx vertx, VertxTestContext testContext,
      String query) {
    HttpServerRequest httpServerRequest = mock(HttpServerRequest.class);
    when(httpServerRequest.method()).thenReturn(HttpMethod.POST);
    RequestContext requestContext = getRequestContext();

    io.vertx.ext.web.RoutingContext routingContext = mock(io.vertx.ext.web.RoutingContext.class);
    when(routingContext.request()).thenReturn(httpServerRequest);
    when(routingContext.getBody()).thenReturn(new BufferImpl().appendString(query));
    when(routingContext.get(eq(RequestContext.KEY))).thenReturn(requestContext);
    when(routingContext.queryParams()).thenReturn(MultiMap.caseInsensitiveMultiMap());
    when(routingContext.parsedHeaders()).thenReturn(new ParsableHeaderValuesContainer(
        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
        Collections.emptyList(),
        new ParsableMIMEValue("application/json")));
    when(routingContext.vertx()).thenReturn(vertx.getDelegate());
    HttpServerResponse responseMock = mockResponse(testContext);
    when(routingContext.response()).thenReturn(responseMock);
    doAnswer(invocation -> {
      testContext.failNow(invocation.getArgument(0));
      return null;
    }).when(routingContext).fail(any());

    RoutingContext rxRoutingContext = mock(RoutingContext.class);
    when(rxRoutingContext.getDelegate()).thenReturn(routingContext);
    when(rxRoutingContext.get(eq(RequestContext.KEY))).thenReturn(requestContext);
    return rxRoutingContext;
  }

  private RequestContext getRequestContext() {
    return new RequestContext(new RequestEvent(new ClientRequest(), new JsonObject()));
  }
}