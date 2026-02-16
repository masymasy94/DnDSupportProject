package com.dndplatform.chat.adapter.inbound.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ChatExceptionMapper implements ExceptionMapper<Exception> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", exception.getMessage()))
                    .build();
        }

        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("BAD_REQUEST", exception.getMessage()))
                    .build();
        }

        if (exception instanceof IllegalStateException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", exception.getMessage()))
                    .build();
        }

        log.log(Level.SEVERE, "Unexpected error", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"))
                .build();
    }

    public record ErrorResponse(String code, String message) {}
}
