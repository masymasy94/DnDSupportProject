package com.dndplatform.common.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Ensures CORS headers are present on all responses, including error responses.
 * This filter acts as a fallback when Quarkus CORS handling doesn't apply headers
 * to certain error responses (e.g., validation errors).
 */
@Provider
public class CorsResponseFilter implements ContainerResponseFilter {

    private static final String ALLOWED_ORIGINS = "http://localhost:3000";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,PATCH";
    private static final String ALLOWED_HEADERS = "accept,authorization,content-type,x-requested-with";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");

        if (origin == null) {
            return;
        }

        // Only add headers if not already present
        if (responseContext.getHeaders().get("Access-Control-Allow-Origin") == null) {
            if (ALLOWED_ORIGINS.contains(origin)) {
                responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            }
        }

        if (responseContext.getHeaders().get("Access-Control-Allow-Methods") == null) {
            responseContext.getHeaders().add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        }

        if (responseContext.getHeaders().get("Access-Control-Allow-Headers") == null) {
            responseContext.getHeaders().add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        }

        if (responseContext.getHeaders().get("Access-Control-Allow-Credentials") == null) {
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        }
    }
}
