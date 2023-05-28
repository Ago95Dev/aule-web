package org.univaq.swa.framework.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

import org.univaq.swa.exceptions.CustomException;

import jakarta.ws.rs.core.UriInfo;

/**
 * @author Ago95Dev
 */

@Path("auth")
public class AuthenticationRes {   
    private static Connection con ;
    public AuthenticationRes() throws SQLException, CustomException {
        con = DBConnection.getConnection();
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doLogin(@Context UriInfo uriinfo,
                            //un altro modo per ricevere e iniettare i parametri con JAX-RS...
                            @FormParam("email") String email,
                            @FormParam("password") String password) {
        try {
            int utente_id = authenticate(email, password);
            System.out.println(utente_id);
            if (utente_id > 0) {
                String authToken = issueToken(uriinfo, email);
                try (PreparedStatement stmt = con.prepareStatement("UPDATE user SET token = ? WHERE id = ?")) {
                    stmt.setString(1, authToken);
                    stmt.setInt(2, utente_id);
                    stmt.executeUpdate();
                }
                return Response.ok(authToken)
                        .cookie(new NewCookie.Builder("token").value(authToken).build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Logged
    @DELETE
    @Path("/logout")
    public Response doLogout(@Context HttpServletRequest request) {
        try {
            //estraiamo i dati inseriti dal nostro LoggedFilter...
            String token = (String) request.getAttribute("token");
            if (token != null) {
                revokeToken(token);
            }
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("refresh")
    @Logged
    public Response refresh(@Context ContainerRequestContext req, @Context UriInfo uriinfo) {
        //proprietà iniettata nella request dal filtro di autenticazione
        String email = (String) req.getProperty("email-field");
        String newtoken = issueToken(uriinfo, email);
        return Response.ok(newtoken)
                .cookie(new NewCookie.Builder("token").value(newtoken).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();
        
    }

    private int authenticate(String email, String password) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT id FROM user WHERE email = ? AND password = ?;")) {
            stmt.setString(1, email);
            
          //  stmt.setString(2, Encryption.encryptPassword(password));
            stmt.setString(2,password);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return 0;
    }

    private String issueToken(UriInfo context, String email) {
        /* registrare il token e associarlo all'utenza */
        return UUID.randomUUID().toString();
    }

    private void revokeToken(String token) {
        try (PreparedStatement stmt = con.prepareStatement("UPDATE user SET token = NULL WHERE token = ?")) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(AuthenticationRes.class.getName()).severe(e.getMessage());
        }
    }
}
