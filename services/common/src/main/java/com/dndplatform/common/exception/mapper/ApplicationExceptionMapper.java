package com.dndplatform.common.exception.mapper;

import com.dndplatform.common.exception.ApplicationException;
import com.dndplatform.common.exception.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * JAX-RS ExceptionMapper that converts ApplicationException instances
 * to proper HTTP responses with standardized error format.
 */
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ApplicationException exception) {
        int statusCode = exception.getStatusCode();
        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(
                statusCode,
                Response.Status.fromStatusCode(statusCode).getReasonPhrase(),
                exception.getMessage(),
                path
        );

        return Response.status(statusCode)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}