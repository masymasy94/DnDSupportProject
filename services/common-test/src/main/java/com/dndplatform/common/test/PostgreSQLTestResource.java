package com.dndplatform.common.test;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

/**
 * Starts a real PostgreSQL container for the test suite via Testcontainers.
 * Registered as a global {@link QuarkusTestResourceLifecycleManager}: annotate any
 * single test class in a module with
 * {@code @QuarkusTestResource(PostgreSQLTestResource.class)} and it applies to
 * ALL tests in that module.
 *
 * <p>The resource overrides the datasource URL, username, and password at runtime,
 * so the static values in {@code application.properties} are placeholders only.
 */
public class PostgreSQLTestResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName IMAGE = DockerImageName.parse("postgres:16-alpine");

    private PostgreSQLContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new PostgreSQLContainer<>(IMAGE)
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        container.start();
        return Map.of(
                "quarkus.datasource.jdbc.url", container.getJdbcUrl(),
                "quarkus.datasource.username", container.getUsername(),
                "quarkus.datasource.password", container.getPassword()
        );
    }

    @Override
    public void stop() {
        if (container != null) {
            container.stop();
            container = null;
        }
    }
}
