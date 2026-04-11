package integration.resource;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

/**
 * Global QuarkusTestResource that starts a real PostgreSQL container for the
 * test suite. Annotated on SmokeTest (any single test class suffices, Quarkus
 * applies test resources globally to the whole module).
 * Overrides quarkus.datasource.jdbc.url/username/password at runtime.
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
