package com.dndplatform.common.client;

import com.dndplatform.common.exception.BadGatewayException;
import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.exception.UnauthorizedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic REST client exception mapper that converts HTTP error responses
 * to appropriate ApplicationException subtypes.
 *
 * This filter parses the standard ErrorResponse JSON format and throws
 * the correct exception based on the HTTP status code.
 *
 * Usage: Register this provider on your REST client interface:
 * <pre>
 * {@code @RegisterProvider(RestClientExceptionMapper.class)}
 * public interface MyRestClient { ... }
 * </pre>
 */
public class RestClientExceptionMapper implements ClientResponseFilter {

    private static final Pattern STATUS_PATTERN = Pattern.compile("\"status\"\\s*:\\s*(\\d+)");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]+)\"");

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        if (responseContext.getStatus() >= 400) {
            String body = new String(responseContext.getEntityStream().readAllBytes());
            ErrorInfo errorInfo = parseErrorResponse(body, responseContext.getStatus());

            // Throw appropriate exception based on HTTP status code
            switch (errorInfo.status) {
                case 400 -> throw new WebApplicationException(errorInfo.message, 400);
                case 401 -> throw new UnauthorizedException(errorInfo.message);
                case 403 -> throw new ForbiddenException(errorInfo.message);
                case 404 -> throw new NotFoundException(errorInfo.message);
                case 409 -> throw new ConflictException(errorInfo.message);
                case 502 -> throw new BadGatewayException(errorInfo.message);
                default -> throw new WebApplicationException(errorInfo.message, errorInfo.status);
            }
        }
    }

    /**
     * Parse error response and extract status code and message using regex.
     * Expected JSON format: {"status":404,"error":"Not Found","message":"Error details","path":"/api/path","timestamp":"..."}
     */
    private ErrorInfo parseErrorResponse(String body, int fallbackStatus) {
        try {
            int status = fallbackStatus;
            String message = body;

            // Extract status code
            Matcher statusMatcher = STATUS_PATTERN.matcher(body);
            if (statusMatcher.find()) {
                status = Integer.parseInt(statusMatcher.group(1));
            }

            // Extract message
            Matcher messageMatcher = MESSAGE_PATTERN.matcher(body);
            if (messageMatcher.find()) {
                message = messageMatcher.group(1);
            }

            return new ErrorInfo(status, message);
        } catch (Exception e) {
            // If parsing fails, return fallback values
            return new ErrorInfo(fallbackStatus, body);
        }
    }

    /**
     * Internal record to hold parsed error information.
     */
    private record ErrorInfo(int status, String message) {}
}

