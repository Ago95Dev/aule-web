package org.univaq.swa.framework.security;

import java.sql.Connection;
import com.mysql.cj.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Ago95Dev
 */

public class DBConnection {
    
    public static String url = "jdbc:mysql://localhost:3306/aule_web";
    public static String user = "root";
    public static String password = "password";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DBConnection.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DBConnection.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DBConnection.password = password;
    }
    
    public static Connection getConnection()
    {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
