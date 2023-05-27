package org.univaq.swa.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class RESTWebApplicationException extends WebApplicationException {

    public RESTWebApplicationException() {
        super(Response.serverError().build());
    }
    
    public RESTWebApplicationException(Exception e) {
        this(500,e.getMessage());
    }

    public RESTWebApplicationException(String message) {
        super(Response.serverError()
                .entity(message)
                .type("text/plain")
                .build());
    }

    public RESTWebApplicationException(int status, String message) {
        super(Response.status(status)
                .entity(message)
                .type("text/plain")
                .build());
    }
}
