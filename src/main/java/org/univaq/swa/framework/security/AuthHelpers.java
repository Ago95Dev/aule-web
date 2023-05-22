package org.univaq.swa.framework.security;

import jakarta.ws.rs.core.UriInfo;
import java.util.UUID;

/**
 *
 * Una classe di utilità di supporto all'autenticazione
 * qui tutto è finto, non usiamo JWT o altre tecnologie
 *
 */
public class AuthHelpers {

    private static AuthHelpers instance = null;

    public AuthHelpers() {

    }

    public boolean authenticateUser(String username, String password) {
        return true;
    }

    public String issueToken(UriInfo context, String username) {        
        String token = username + UUID.randomUUID().toString();
        return token;
    }

    public void revokeToken(String token) {
        /* invalidate il token */
    }

    public String validateToken(String token) {
        return "pippo"; //lo username andrebbe derivato dal token!
    }

    public static AuthHelpers getInstance() {
        if (instance == null) {
            instance = new AuthHelpers();
        }
        return instance;
    }

}
