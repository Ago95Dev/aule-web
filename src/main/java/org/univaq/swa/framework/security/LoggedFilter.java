package org.univaq.swa.framework.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Provider
@Logged
@Priority(Priorities.AUTHENTICATION)
public class LoggedFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(@Context ContainerRequestContext requestContext) throws IOException {
        String token = null;
        final String path = requestContext.getUriInfo().getAbsolutePath().toString();

        //come esempio, proviamo a cercare il token in vari punti, in ordine di priorità
        //in un'applicazione reale, potremmo scegliere una sola modalità
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring("Bearer".length()).trim();
        } else if (requestContext.getCookies().containsKey("token")) {
            token = requestContext.getCookies().get("token").getValue();
        } else if (requestContext.getUriInfo().getQueryParameters().containsKey("token")) {
            token = requestContext.getUriInfo().getQueryParameters().getFirst("token");
        }
        if (token != null && !token.isEmpty()) {
            try {
                //validiamo il token
                final String user = validateToken(token);
                if (user != null) {
                    //inseriamo nel contesto i risultati dell'autenticazione
                    //per farli usare dai nostri metodi restful
                    //iniettando @Context ContainerRequestContext
                    requestContext.setProperty("token", token);
                    requestContext.setProperty("user", user);
                    //OPPURE
                    // https://dzone.com/articles/custom-security-context-injax-rs
                    //mettiamo i dati anche nel securitycontext standard di JAXRS...
                    //che può essere iniettato con @Context SecurityContext nei metodi
                    //final SecurityContext originalSecurityContext = requestContext.getSecurityContext();
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {
                                @Override
                                public String getName() {
                                    return user;
                                }
                            };
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            //qui andrebbe verificato se l'utente ha il ruolo richiesto
                            return true;
                        }

                        @Override
                        public boolean isSecure() {
                            return path.startsWith("https");
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return "Token-Based-Auth-Scheme";
                        }
                    });

                } else {
                    //se non va bene... 
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private String validateToken(String token) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/collector_site?noAccessToProcedureBodies=true&serverTimezone=Europe/Rome", "collectorsite","Collectorsite0@");
            String query = "SELECT id FROM utente_rest WHERE token = '" + token + "'";
            PreparedStatement pst = con.prepareStatement( query );
            ResultSet rs = pst.executeQuery();

            if( rs.next() ) {
                return rs.getString("id");
            }
            else { return ""; }
        }
        catch (Exception ex) { return ""; }
    }
}

