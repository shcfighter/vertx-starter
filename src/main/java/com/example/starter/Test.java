package com.example.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Test {
  public static void main(String[] args) {
    Vertx.vertx().deployVerticle("com.example.starter.MysqlVerticle", new DeploymentOptions().setInstances(4));
  }
}
