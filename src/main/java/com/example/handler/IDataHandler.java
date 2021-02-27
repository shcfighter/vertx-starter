package com.example.handler;

import com.example.handler.impl.DataHandler;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

@ProxyGen
@VertxGen
public interface IDataHandler {

  // A couple of factory methods to create an instance and a proxy
  /*static IDataHandler create(Vertx vertx) {
    return new DataHandler(vertx);
  }

  static IDataHandler createProxy(Vertx vertx, String address) {
    return new IDataHandlerVertxEBProxy(vertx, address);
  }*/

  @Fluent
  IDataHandler search(Handler<AsyncResult<JsonArray>> handler);

}
