package org.univaq.swa.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author didattica
 */
@Provider
public class JacksonExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException exception) {
        //utile per catturare tutte le eccezioni derivanti dalla serializzazione/deserializzazione automatica di oggetti
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON").build();
    }
}
