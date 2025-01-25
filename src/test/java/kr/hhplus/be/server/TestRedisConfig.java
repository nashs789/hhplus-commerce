package kr.hhplus.be.server;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestRedisConfig {
    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    public static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());
    }

    @PreDestroy
    public void preDestroy() {
        if (REDIS_CONTAINER.isRunning()) {
            REDIS_CONTAINER.stop();
        }
    }

}
