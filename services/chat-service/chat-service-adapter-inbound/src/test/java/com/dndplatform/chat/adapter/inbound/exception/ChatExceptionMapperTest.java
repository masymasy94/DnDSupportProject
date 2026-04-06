package com.dndplatform.chat.adapter.inbound.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatExceptionMapperTest {

    private ChatExceptionMapper sut;

    @BeforeEach
    void setUp() {
        sut = new ChatExceptionMapper();
    }

    @Test
    void shouldReturn404ForNotFoundException() {
        var exception = new NotFoundException("Resource not found");

        Response result = sut.toResponse(exception);

        assertThat(result.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldReturn400ForIllegalArgumentException() {
        var exception = new IllegalArgumentException("Invalid argument");

        Response result = sut.toResponse(exception);

        assertThat(result.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldReturn401ForIllegalStateException() {
        var exception = new IllegalStateException("Unauthorized state");

        Response result = sut.toResponse(exception);

        assertThat(result.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    void shouldReturn500ForUnexpectedException() {
        var exception = new RuntimeException("Something went wrong");

        Response result = sut.toResponse(exception);

        assertThat(result.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}
