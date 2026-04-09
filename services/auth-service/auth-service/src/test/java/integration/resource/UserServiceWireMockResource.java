package integration.resource;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class UserServiceWireMockResource implements QuarkusTestResourceLifecycleManager {

    private static WireMockServer server;

    @Override
    public Map<String, String> start() {
        server = new WireMockServer(wireMockConfig().port(18089));
        server.start();

        // Default stubs for all user-service interactions.
        // Specific tests can override these inside their @Test methods using static WireMock methods.

        // Credentials validation → returns a valid user (for login)
        // Pattern tolerates optional trailing slash — the REST client may add one.
        server.stubFor(post(urlPathMatching("/users/credentials-validation/?"))
            .willReturn(okJson("""
                {"id":1,"username":"gandalf","email":"gandalf@shire.com","role":"PLAYER","active":true,"createdAt":"2026-01-01T00:00:00"}
                """)));

        // Email lookup → returns a user
        server.stubFor(post(urlPathMatching("/users/email-lookup/?"))
            .willReturn(okJson("""
                {"id":1,"username":"gandalf","email":"gandalf@shire.com","role":"PLAYER","active":true,"createdAt":"2026-01-01T00:00:00"}
                """)));

        // Password update → 204 No Content
        server.stubFor(put(urlPathMatching("/users/\\d+/password"))
            .willReturn(aResponse().withStatus(204)));

        return Map.of(
            "quarkus.rest-client.rest_client_user_service.url", server.baseUrl(),
            "rest-client.url", server.baseUrl()
        );
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public static WireMockServer getServer() {
        return server;
    }
}
