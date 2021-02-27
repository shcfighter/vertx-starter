package com.example.handler.impl;

import com.example.handler.IDataHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;

public class DataHandler implements IDataHandler {

  private PgPool pgClient;
  private Vertx vertx;

  public DataHandler(Vertx vertx) {
    this.vertx = vertx;

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("127.0.0.1")
      .setDatabase("postgres")
      .setUser("postgres")
      .setPassword("h123456");
    PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
    pgClient = PgPool.pool(this.vertx, connectOptions, poolOptions);
  }

  @Override
  public IDataHandler search(Handler<AsyncResult<JsonArray>> handler) {
    Promise promise = Promise.promise();
    // Execute the query
    pgClient.getConnection().compose(conn -> conn
      .preparedQuery("SELECT * FROM PUBLIC.bodyguard_config limit 10")
      .execute().onSuccess(rows -> conn.close())).onSuccess(rows -> {
      // Success
      JsonArray jsonArray = new JsonArray();
      RowIterator<Row> iterator = rows.iterator();
      while (iterator.hasNext()) {
        Row row = iterator.next();
        jsonArray.add(row.toJson());
      }
      promise.complete(jsonArray);
      handler.handle(promise.future());
    }).onFailure(err -> {
      // Failed
      System.out.println("Failure: " + err);
    });
    return this;
  }
}
