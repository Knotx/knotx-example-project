package com.acme.datasource.adapter;

import static com.google.common.io.Resources.getResource;
import static io.knotx.junit5.util.RequestUtil.subscribeToResult_shouldSucceed;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.knotx.databridge.api.DataSourceAdapterRequest;
import io.knotx.databridge.api.DataSourceAdapterResponse;
import io.knotx.junit5.KnotxApplyConfiguration;
import io.knotx.junit5.KnotxExtension;
import io.knotx.reactivex.databridge.api.DataSourceAdapterProxy;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(KnotxExtension.class)
public class ExampleDataSourceAdapterProxyTest {

  private static final String KNOTX_BRIDGE_DATASOURCE_CUSTOM = "knotx.bridge.datasource.custom";

  @BeforeAll
  public static void init() throws SQLException, ClassNotFoundException, IOException {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");

    // initialize database
    initDatabase();
  }

  @Test
  @KnotxApplyConfiguration("application.conf")
  public void callCustomDataSourceAdapter_validQuery(
      VertxTestContext context, Vertx vertx)
      throws IOException, URISyntaxException {

    callWithAssertions(context, vertx, "SELECT * FROM employee",
        response -> {
          Assertions.assertEquals(2, toJsonArray(response).size());
        });
  }

  private JsonArray toJsonArray(DataSourceAdapterResponse response) {
    return new JsonArray(response.getResponse()
                                 .getBody()
                                 .toString());
  }

  private void callWithAssertions(
      VertxTestContext context, Vertx vertx, String query,
      Consumer<io.knotx.databridge.api.DataSourceAdapterResponse> onSuccess) {
    DataSourceAdapterRequest request = prepareRequest(query);

    rxProcessWithAssertions(context, vertx, onSuccess, request);
  }

  private void rxProcessWithAssertions(VertxTestContext context, Vertx vertx,
      Consumer<DataSourceAdapterResponse> onSuccess, DataSourceAdapterRequest request) {
    DataSourceAdapterProxy service = DataSourceAdapterProxy.createProxy(vertx,
        KNOTX_BRIDGE_DATASOURCE_CUSTOM);
    Single<DataSourceAdapterResponse> adapterResponse = service.rxProcess(request);

    subscribeToResult_shouldSucceed(context, adapterResponse, onSuccess);
  }

  private DataSourceAdapterRequest prepareRequest(String query) {
    return new DataSourceAdapterRequest()
        .setParams(prepareParams(query));
  }


  private JsonObject prepareParams(String query) {
    return new JsonObject().put("query", query);
  }

  private static void initDatabase() throws SQLException, IOException {
    try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(getDDL());
      connection.commit();
      statement.executeUpdate(getData());
      connection.commit();
    }
  }

  private static String getDDL() throws IOException {
    return Resources.toString(getResource("sampleDB.ddl"), Charsets.UTF_8);
  }

  private static String getData() throws IOException {
    return Resources.toString(getResource("sampleDB.sql"), Charsets.UTF_8);
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
  }
}
