package com.example.starter;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * @author shc_fighter
 * created on 2021-01-29
 */
public class ServerVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

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

  }

  public void searchHandler(RoutingContext ctx) {
    vertx.eventBus().rxRequest("data_search", new JsonObject().put("id", "1111")).subscribe(message -> {
      ctx.response().setStatusCode(200)
        .putHeader("content-type", "application/json")
        .end(String.valueOf(message.body()));
    });
  }

}
