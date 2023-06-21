package org.univaq.swa.framework.security;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Malformed Payload")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
