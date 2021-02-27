package com.example.starter;

import com.example.handler.IDataHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceProxyBuilder;

public class EventBusVerticle extends AbstractVerticle {

  private IDataHandler dataHandler;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ServiceProxyBuilder builder = new ServiceProxyBuilder(vertx).setAddress("database-service-address");
    dataHandler = builder.build(IDataHandler.class);

    vertx.eventBus().consumer("data_search", this::searchHandler);
  }

  public void searchHandler(Message<JsonObject> message) {
    dataHandler.search(handler -> {
      JsonArray jsonArray = handler.result();
      message.reply(jsonArray);
    });
  }
}
