package com.dndplatform.user.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;

import java.io.IOException;

@ApplicationScoped
public class RestClientExceptionMapper implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {

        if (clientResponseContext.getStatus() >= 400) {
            String body = new String(clientResponseContext.getEntityStream().readAllBytes());
            throw new WebApplicationException(body, clientResponseContext.getStatus());
        }
    }
}
