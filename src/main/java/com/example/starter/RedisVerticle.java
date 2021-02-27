package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    // Create the redis client
    List list = new ArrayList();
    list.add("redis://127.0.0.1:6379");
    Redis client = Redis.createClient(vertx, new RedisOptions().setEndpoints(list));
    RedisAPI redis = RedisAPI.api(client);

    client.connect()
      .compose(conn -> {
        return redis.set(Arrays.asList("key2", "value2"))
          .compose(v -> {
            System.out.println("key stored");
            return redis.get("key");
          });
      }).onComplete(ar -> {
      if (ar.succeeded()) {
        startFuture.complete();
        System.out.println("Retrieved value: " + ar.result());
      } else {
        System.out.println("Connection or Operation Failed " + ar.cause());
      }
    });
    startFuture.future().compose(o -> {
      redis.get("key2", handler -> {
        System.out.println("key2: " + handler.result());
      });
      return Promise.promise().future();
    });

  }


}
