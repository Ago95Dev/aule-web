package org.univaq.swa.framework.security;

import jakarta.ws.rs.core.UriInfo;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * Una classe di utilità di supporto all'autenticazione
 * qui tutto è finto, non usiamo JWT o altre tecnologie
 *
 */
public class AuthHelpers {

    private static AuthHelpers instance = null;
    private static Connection con ;

    public AuthHelpers() {

    }

    public int authenticate(String email, String password) {
        /*try (PreparedStatement stmt = con.prepareStatement("SELECT id FROM user WHERE email = ? AND password = ?;")) {
            stmt.setString(1, email);
            
          //  stmt.setString(2, Encryption.encryptPassword(password));
            stmt.setString(2,password);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
             e.printStackTrace();
        } */
        return 0;
    }

    public String issueToken(UriInfo context, String username) {        
        return UUID.randomUUID().toString(); 
    }

    public void revokeToken(String token) {
        /* invalidate il token 
        try (PreparedStatement stmt = con.prepareStatement("UPDATE user SET token = NULL WHERE token = ?")) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(AuthenticationRes.class.getName()).severe(e.getMessage());
        }*/
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
