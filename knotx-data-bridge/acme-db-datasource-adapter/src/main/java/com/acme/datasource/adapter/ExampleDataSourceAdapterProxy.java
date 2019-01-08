package com.acme.datasource.adapter;

import io.knotx.databridge.api.DataSourceAdapterRequest;
import io.knotx.databridge.api.DataSourceAdapterResponse;
import io.knotx.databridge.api.reactivex.AbstractDataSourceAdapterProxy;
import io.knotx.dataobjects.ClientResponse;
import io.reactivex.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

public class ExampleDataSourceAdapterProxy extends AbstractDataSourceAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDataSourceAdapterProxy.class);

  //we will need JDBC Client here to perform DB queries
  private final JDBCClient client;

  public ExampleDataSourceAdapterProxy(JDBCClient client) {
    this.client = client;
  }

  @Override
  protected Single<DataSourceAdapterResponse> processRequest(
      DataSourceAdapterRequest adapterRequest) {
    final String query = adapterRequest.getParams()
                                       .getString("query");
    LOGGER.debug("Processing request with query: `{}`", query);
    return client.rxGetConnection()
                 .flatMap(
                     sqlConnection -> sqlConnection.rxQuery(query)
                 )
                 .map(this::toAdapterResponse);
  }

  private DataSourceAdapterResponse toAdapterResponse(ResultSet rs) {
    return new DataSourceAdapterResponse().setResponse(
        new ClientResponse().setBody(body(rs))
    );
  }

  private Buffer body(ResultSet rs){
    return Buffer.buffer(new JsonArray(rs.getRows()).encode());
  }
}
