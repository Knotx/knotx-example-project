package com.project.test.mock;

import graphql.execution.ExecutionId;
import graphql.execution.ExecutionTypeInfo;
import graphql.language.Field;
import graphql.language.FragmentDefinition;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DummyDataFetchingEnvironment implements DataFetchingEnvironment {

  @Override
  public <T> T getSource() {
    return null;
  }

  @Override
  public Map<String, Object> getArguments() {
    return Collections.emptyMap();
  }

  @Override
  public boolean containsArgument(String name) {
    return false;
  }

  @Override
  public <T> T getArgument(String name) {
    return null;
  }

  @Override
  public <T> T getContext() {
    return null;
  }

  @Override
  public <T> T getRoot() {
    return null;
  }

  @Override
  public GraphQLFieldDefinition getFieldDefinition() {
    return null;
  }

  @Override
  public List<Field> getFields() {
    return Collections.emptyList();
  }

  @Override
  public GraphQLOutputType getFieldType() {
    return null;
  }

  @Override
  public ExecutionTypeInfo getFieldTypeInfo() {
    return null;
  }

  @Override
  public GraphQLType getParentType() {
    return null;
  }

  @Override
  public GraphQLSchema getGraphQLSchema() {
    return null;
  }

  @Override
  public Map<String, FragmentDefinition> getFragmentsByName() {
    return Collections.emptyMap();
  }

  @Override
  public ExecutionId getExecutionId() {
    return null;
  }

  @Override
  public DataFetchingFieldSelectionSet getSelectionSet() {
    return null;
  }
}
