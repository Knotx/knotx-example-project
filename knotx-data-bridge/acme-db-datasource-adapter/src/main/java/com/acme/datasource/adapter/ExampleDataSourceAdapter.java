package com.acme.datasource.adapter;

import io.knotx.databridge.api.DataSourceAdapterProxy;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleDataSourceAdapter extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDataSourceAdapter.class);

  private MessageConsumer<JsonObject> consumer;

  private ExampleDataSourceOptions configuration;

  private ServiceBinder serviceBinder;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    configuration = new ExampleDataSourceOptions(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());

    //create JDBC Clinet here and pass it to DataSourceAdapterProxy - notice using clientOptions property here
    final JDBCClient client = JDBCClient.createShared(vertx, configuration.getClientOptions());

    //register the service proxy on event bus
    serviceBinder = new ServiceBinder(getVertx());
    consumer = serviceBinder
        .setAddress(configuration.getAddress())
        .register(DataSourceAdapterProxy.class, new ExampleDataSourceAdapterProxy(client));
  }

  @Override
  public void stop() throws Exception {
    serviceBinder.unregister(consumer);
  }
}
