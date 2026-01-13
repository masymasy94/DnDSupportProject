package com.dndplatform.user.client.authorization;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.logging.Logger;

import static com.dndplatform.user.client.ClientConfig.CLIENT_CONFIG_KEY;
import static java.lang.String.format;

@ApplicationScoped
public class AuthorizationFilter implements ClientRequestFilter {

    private final Logger log = Logger.getLogger(getClass().getName());
    private String serviceTokenKey;
    private String serviceTokenVal;

    @PostConstruct
    void init(){
        log.info(() -> format(
                "%s created: custom auth header is %s, auth key starts with %s...",
                getClass().getSimpleName(),
                serviceTokenKey,
                serviceTokenVal.substring(0, 10).concat("...")
        ));
    }


    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        clientRequestContext.getHeaders().put(serviceTokenKey, List.of(serviceTokenVal));
    }

    @Inject
    public AuthorizationFilter setServiceTokenVal(
            @ConfigProperty(name = CLIENT_CONFIG_KEY+"_auth") String serviceTokenVal) {
        this.serviceTokenVal = serviceTokenVal;
        return this;
    }

    @Inject
    public AuthorizationFilter setServiceTokenKey(
            @ConfigProperty(name = CLIENT_CONFIG_KEY + "_auth_http_header", defaultValue = "x-service-token")
            String serviceTokenKey) {
        this.serviceTokenKey = serviceTokenKey;
        return this;
    }
}
