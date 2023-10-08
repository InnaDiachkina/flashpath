package com.flashpath.config;

import org.testcontainers.containers.GenericContainer;

public class CustomRedisContainer extends GenericContainer<CustomRedisContainer> {
    private static final String REDIS_IMAGE = "redis:latest";
    private static  CustomRedisContainer redisContainer;

    public CustomRedisContainer() {
        super(REDIS_IMAGE);
        this.withExposedPorts(6379);
    }

     public static synchronized CustomRedisContainer getInstance() {
        if (redisContainer == null) {
            redisContainer = new CustomRedisContainer();
        }
        return redisContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.data.redis.host", redisContainer.getContainerIpAddress());
        System.setProperty("spring.data.redis.port", String.valueOf(getMappedPort(6379)));
    }

    @Override
    public void stop() {
    }
}
