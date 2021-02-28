package com.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.Iterator;

public class PgVerticle extends AbstractVerticle {

  PgPool pgClient;
  int i = 0;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("127.0.0.1")
      .setDatabase("postgres")
      .setUser("postgres")
      .setPassword("h123456");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(10);

    pgClient = PgPool.pool(vertx, connectOptions, poolOptions);

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

  public void searchHandler(RoutingContext routingContext) {
    pgClient.getConnection().compose(conn -> {
      // All operations execute on the same connection
      return conn
        .query("SELECT * FROM PUBLIC.bodyguard_config limit 10")
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
        i++;
        response.end(jsonArray.encodePrettily());
        //System.out.println(Thread.currentThread().getName() + "  " + i);
      } else {
        System.out.println("Something went wrong " + ar.cause().getMessage());
      }
    });
  }
}
