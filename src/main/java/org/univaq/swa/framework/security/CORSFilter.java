package org.univaq.swa.framework.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;


/**
 *
 * @author didattica
 */
@Provider
@PreMatching
public class CORSFilter implements ContainerResponseFilter, ContainerRequestFilter {

    //ContainerResponseFilter: inserisce questi header in tutte le risposte
    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        //permettiamo tutte le connessioni cross-origin (esterne)
        responseContext.getHeaders().add(
                "Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Credentials", "*");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Headers",
                "*,Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Custom-header,X-Requested-With, content-type, access-control-allow-origin, access-control-allow-methods, access-control-allow-headers");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS, PUT, DELETE, HEAD");
        responseContext.getHeaders().add(
                "Access-Control-Expose-Headers",
                "Location,Authorization, *");

    }

    //ContainerRequestFilter: intercetta e gestisce direttamente le richieste con metodo OPTIONS
    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        //diamo l'ok a tutte le richieste di preflight inviate dai client prima dell'operazione effettiva
        //senza dover necessariamente implementare il metodo OPTIONS a livello di logica
        if (request.getHeaderString("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS")) {
            request.abortWith(Response.ok().build());
        }
    }
}
