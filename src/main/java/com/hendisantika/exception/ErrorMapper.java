package com.hendisantika.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.CompositeException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * Project : quarkus-reactive-crud-active-pattern
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/17/23
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
@Provider
public class ErrorMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(ErrorMapper.class.getName());

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("Failed to handle request", exception);

        Throwable throwable = exception;

        int code = 500;
        if (throwable instanceof WebApplicationException) {
            code = ((WebApplicationException) exception).getResponse().getStatus();
        }

        // This is a Mutiny exception and it happens
        if (throwable instanceof CompositeException) {
            throwable = throwable.getCause();
        }

        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("exceptionType", throwable.getClass().getName());
        exceptionJson.put("code", code);

        if (exception.getMessage() != null) {
            exceptionJson.put("error", throwable.getMessage());
        }

        return Response.status(code)
                .entity(exceptionJson.toPrettyString())
                .build();
    }
}
