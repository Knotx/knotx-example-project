package io.knotx.example.books.data;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.knotx.fragments.api.Fragment;
import io.knotx.fragments.task.api.Task;
import io.knotx.fragments.task.engine.FragmentEvent;
import io.knotx.fragments.task.engine.FragmentEventContext;
import io.knotx.fragments.task.engine.FragmentEventContextTaskAware;
import io.knotx.fragments.task.engine.FragmentsEngine;
import io.knotx.fragments.task.factory.api.TaskFactory;
import io.knotx.fragments.task.factory.api.metadata.TaskWithMetadata;
import io.knotx.fragments.task.factory.generic.DefaultTaskFactory;
import io.knotx.server.api.context.ClientRequest;
import io.knotx.server.api.context.RequestContext;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.Collections;

import java.util.concurrent.CompletableFuture;

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
  private final TaskFactory taskFactory;

  TaskDataFetcher(Vertx vertx, JsonObject config, RoutingContext routingContext, String taskName) {
    this.vertx = vertx;
    this.config = config;
    this.routingContext = routingContext;
    this.taskName = taskName;
    engine = new FragmentsEngine(vertx);
    taskFactory = new DefaultTaskFactory().configure(config.getJsonObject("taskFactory"), vertx);
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

    TaskWithMetadata task = taskFactory.newInstance(fragment, clientRequest);

    return new FragmentEventContextTaskAware(task.getTask(), eventContext);
  }

  private Fragment createFragment(DataFetchingEnvironment env) {
    JsonObject fragmentConfig = new JsonObject();
    fragmentConfig.put("data-knotx-task", taskName);
    fragmentConfig.put("gql", new JsonObject(env.getArguments()));

    return new Fragment(FRAGMENT_TYPE, fragmentConfig, "");
  }

  abstract T getDataObjectFromJson(JsonObject json, DataFetchingEnvironment environment) throws IllegalAccessException, InstantiationException;
}
