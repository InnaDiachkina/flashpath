package com.flashpath.config;

import org.testcontainers.containers.MongoDBContainer;

public class CustomMongoDbContainer extends MongoDBContainer {
    private static final String DB_IMAGE = "mongo:latest";
    private static CustomMongoDbContainer mongoDbContainer;

    public CustomMongoDbContainer() {
        super(DB_IMAGE);
        withExposedPorts(27017);
    }

    public static synchronized CustomMongoDbContainer getInstance() {
        if (mongoDbContainer == null) {
            mongoDbContainer = new CustomMongoDbContainer();
        }
        return mongoDbContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
        System.setProperty("spring.data.mongodb.database", "testmydatabase");
        System.setProperty("spring.data.mongodb.username", "root");
        System.setProperty("spring.data.mongodb.password", "secret");
    }

    @Override
    public void stop() {
    }
}
