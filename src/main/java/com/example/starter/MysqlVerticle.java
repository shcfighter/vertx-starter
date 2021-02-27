package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.Iterator;

public class MysqlVerticle extends AbstractVerticle {

  MySQLPool client;
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(3306)
      .setHost("localhost")
      .setDatabase("test")
      .setUser("root")
      .setPassword("Hh123456");

    // Pool options
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(10);

    // Create the client pool
    client = MySQLPool.pool(vertx, connectOptions, poolOptions);

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/mysql").handler(this::searchHandler);
    router.route().handler(ctx -> {

      // This handler will be called for every request
      HttpServerResponse response = ctx.response();
      response.putHeader("content-type", "text/plain");

      // Write to the response and end it
      response.end("Hello World from Vert.x-Web!");
    });


    server.requestHandler(router).listen(8888);
    System.out.println("HTTP server started on port 8888");

    startPromise.complete();
  }

  public void searchHandler(RoutingContext routingContext){
    client.getConnection().compose(conn -> {
      // All operations execute on the same connection
      return conn
        .query("SELECT * FROM bodyguard_config limit 10")
        .execute()
        .onComplete(ar -> {
          // Release the connection to the pool
          conn.close();
        });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");

        JsonArray jsonArray = new JsonArray();
        RowSet<Row> rs = ar.result();
        Iterator<Row> rowIterator = rs.iterator();
        while (rowIterator.hasNext()) {
          Row row = rowIterator.next();
          jsonArray.add(row.toJson());
        }
        // Write to the response and end it
        response.end(jsonArray.encodePrettily());
        //System.out.println("Done");
      } else {
        System.out.println("Something went wrong " + ar.cause().getMessage());
      }
    });
  }
}
