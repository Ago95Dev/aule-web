package org.univaq.swa.framework.security;

/**
 * @author Ago95Dev
 */

public class DBConnection {
    private static final String DB_NAME = "aule_web";
    protected static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME + "?noAccessToProcedureBodies=true" + "&serverTimezone=Europe/Rome";
    protected static final String DB_USER = "Aule";
    protected static final String DB_PASSWORD = "Aule";
}
