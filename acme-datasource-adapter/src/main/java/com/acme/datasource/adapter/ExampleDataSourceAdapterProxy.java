package com.acme.datasource.adapter;

import io.knotx.databridge.api.DataSourceAdapterRequest;
import io.knotx.databridge.api.DataSourceAdapterResponse;
import io.knotx.databridge.api.reactivex.AbstractDataSourceAdapterProxy;
import io.knotx.dataobjects.ClientResponse;
import io.reactivex.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ExampleDataSourceAdapterProxy extends AbstractDataSourceAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDataSourceAdapterProxy.class);

  @Override
  protected Single<DataSourceAdapterResponse> processRequest(
      DataSourceAdapterRequest adapterRequest) {
    final String message = adapterRequest.getParams()
                                         .getString("message");
    LOGGER.info("Processing request with message: `{}`", message);
    return prepareResponse(message);
  }

  private Single<DataSourceAdapterResponse> prepareResponse(String message) {
    final DataSourceAdapterResponse response = new DataSourceAdapterResponse();
    final ClientResponse clientResponse = new ClientResponse();
    clientResponse.setBody(Buffer.buffer("{\"message\":\"" + message + "\"}"));
    response.setResponse(clientResponse);
    return Single.just(response);
  }


}
