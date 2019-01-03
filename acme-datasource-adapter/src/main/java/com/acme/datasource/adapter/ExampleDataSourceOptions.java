package com.acme.datasource.adapter;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, publicConverter = false)
public class ExampleDataSourceOptions {

  private String address;

  public ExampleDataSourceOptions(JsonObject config) {
    ExampleDataSourceOptionsConverter.fromJson(config, this);
  }

  public String getAddress() {
    return address;
  }

  public ExampleDataSourceOptions setAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ExampleDataSourceOptionsConverter.toJson(this, json);
    return json;
  }
}
