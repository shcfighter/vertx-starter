package com.example.verticle;

import com.example.handler.IDataHandler;
import com.example.handler.impl.DataHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    /*DataHandler dataHandler = new DataHandler(vertx);
    new ServiceBinder(vertx).setAddress("database-service-address").register(IDataHandler.class, dataHandler);

    vertx.deployVerticle(ServerVerticle.class.getName(), new DeploymentOptions().setInstances(1));
    vertx.deployVerticle(EventBusVerticle.class.getName(), new DeploymentOptions().setInstances(4));*/

    //vertx.deployVerticle(RedisVerticle.class.getName());

    vertx.deployVerticle(PgVerticle.class.getName(), new DeploymentOptions().setInstances(1));

    startPromise.complete();
  }
}
