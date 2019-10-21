package io.knotx.example.books.data;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.knotx.fragments.api.Fragment;
import io.knotx.fragments.engine.FragmentEvent;
import io.knotx.fragments.engine.FragmentEventContext;
import io.knotx.fragments.engine.FragmentEventContextTaskAware;
import io.knotx.fragments.engine.FragmentsEngine;
import io.knotx.fragments.engine.Task;
import io.knotx.fragments.handler.action.ActionProvider;
import io.knotx.fragments.handler.api.ActionFactory;
import io.knotx.fragments.handler.options.FragmentsHandlerOptions;
import io.knotx.fragments.task.TaskBuilder;
import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.RequestContext;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.Collections;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * {@link DataFetcher} that executes {@link Task} to fetch data for {@link graphql.GraphQL}.
 * Expects final data to be in {@Link Fragment} payload under "fetchedData" key.
 * Provides {@link #getDataObjectFromJson(JsonObject, DataFetchingEnvironment)} abstract method, used for transforming {@link JsonObject} into {@link GraphQLDataObject}
 * @param <T> type to return to {@link graphql.GraphQL}
 */
public abstract class TaskDataFetcher<T> implements DataFetcher<CompletableFuture<T>> {

  private static final String FRAGMENT_TYPE = "graphql-data";

  private final Vertx vertx;
  private final JsonObject config;
  private final RoutingContext routingContext;
  private final String taskName;
  private final FragmentsEngine engine;
  private final TaskBuilder taskBuilder;

  TaskDataFetcher(Vertx vertx, JsonObject config, RoutingContext routingContext, String taskName) {
    this.vertx = vertx;
    this.config = config;
    this.routingContext = routingContext;
    this.taskName = taskName;
    engine = new FragmentsEngine(vertx);
    taskBuilder = initTaskBuilder(config, vertx);
  }

  @Override
  public CompletableFuture<T> get(DataFetchingEnvironment environment) {
    FragmentEventContextTaskAware eventContextTaskAware = setupTask(vertx, config, routingContext, environment);
    CompletableFuture<T> future = new CompletableFuture<>();

    engine
        .execute(Collections.singletonList(eventContextTaskAware))
        .subscribe(events -> {
          JsonObject payload = events.get(0).getFragment().getPayload();
          JsonObject fetchedData = payload.getJsonObject("fetchedData");
          future.complete(getDataObjectFromJson(fetchedData, environment));
        });

    return future;
  }

  private FragmentEventContextTaskAware setupTask(Vertx vertx, JsonObject config, RoutingContext routingContext, DataFetchingEnvironment env) {
    Fragment fragment = createFragment(env);

    RequestContext requestContext = routingContext.get(RequestContext.KEY);
    ClientRequest clientRequest = requestContext.getRequestEvent().getClientRequest();

    FragmentEvent event = new FragmentEvent(fragment);
    FragmentEventContext eventContext = new FragmentEventContext(event, clientRequest);

    Task task = taskBuilder
        .build(fragment)
        .orElseThrow(() -> new IllegalStateException("No task built from fragment:\n" + fragment.toString()));

    return new FragmentEventContextTaskAware(task, eventContext);
  }

  private Fragment createFragment(DataFetchingEnvironment env) {
    JsonObject fragmentConfig = new JsonObject();
    fragmentConfig.put(FRAGMENT_TYPE, taskName);
    fragmentConfig.put("gql", new JsonObject(env.getArguments()));

    return new Fragment(FRAGMENT_TYPE, fragmentConfig, "");
  }

  private TaskBuilder initTaskBuilder(JsonObject config, Vertx vertx) {
    FragmentsHandlerOptions options = new FragmentsHandlerOptions(config);
    ActionProvider proxyProvider = new ActionProvider(options.getActions(), supplyFactories(), vertx.getDelegate());
    return new TaskBuilder(FRAGMENT_TYPE, options.getTasks(), proxyProvider);
  }

  private Supplier<Iterator<ActionFactory>> supplyFactories() {
    return () -> {
      ServiceLoader<ActionFactory> factories = ServiceLoader
          .load(ActionFactory.class);
      return factories.iterator();
    };
  }

  abstract T getDataObjectFromJson(JsonObject json, DataFetchingEnvironment environment) throws IllegalAccessException, InstantiationException;
}
