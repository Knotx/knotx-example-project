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
package io.knotx.example.books.handler.rx;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.knotx.example.books.data.TaskArrayDataFetcher;
import io.knotx.example.books.data.TaskSingleDataFetcher;
import io.knotx.example.books.data.model.Book;
import io.knotx.server.api.handler.RoutingHandlerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.graphql.GraphQLHandler;
import java.io.InputStreamReader;
import java.io.Reader;

public class GraphQLHandlerFactory implements RoutingHandlerFactory {

  @Override
  public String getName() {
    return "graphqlHandler";
  }

  @Override
  public Handler<RoutingContext> create(Vertx vertx, JsonObject config) {
    return routingContext -> {
      GraphQL graphQL = setupGraphQL(vertx, config, routingContext);
      GraphQLHandler
          .create(graphQL)
          .handle(routingContext);
    };
  }

  private static GraphQL setupGraphQL(Vertx vertx, JsonObject config, RoutingContext routingContext) {
    Reader schema = loadResource(config.getString("schema"));

    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    RuntimeWiring runtimeWiring = newRuntimeWiring()
        .type("QueryType", builder -> builder
            .dataFetcher("books", new TaskArrayDataFetcher<>("get-books", Book.class, vertx, config, routingContext, json -> json.getJsonArray("items")))
            .dataFetcher("book", new TaskSingleDataFetcher<>("get-book", Book.class, vertx, config, routingContext))
//            .dataFetcher("javaBooks", new TaskArrayDataFetcher<>("get-java-books", Book.class, vertx, config, routingContext, json -> json.getJsonArray("items")))
//            .dataFetcher("cSharpBooks", new TaskArrayDataFetcher<>("get-csharp-books", Book.class, vertx, config, routingContext, json -> json.getJsonArray("items")))
//            .dataFetcher("javaBasicsBook", new TaskSingleDataFetcher<>("get-java-basics-book", Book.class, vertx, config, routingContext))
        )
        .build();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator
        .makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

    return GraphQL.newGraphQL(graphQLSchema).build();
  }

  private static Reader loadResource(String path) {
    return new InputStreamReader(GraphQLHandlerFactory.class.getResourceAsStream("/" + path));
  }

}
