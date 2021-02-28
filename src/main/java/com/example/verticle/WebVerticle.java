package com.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class WebVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    server.requestHandler(router).listen(8080);

    router.route().handler(ctx -> {
      // do something if the request is for *.vertx.io
      HttpServerResponse response = ctx.response();
      response.putHeader("content-type", "application/json");
      response.end(new JsonObject().put("key", "value").encodePrettily());
    });

    startPromise.complete();
  }
}
