package org.univaq.swa.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.univaq.swa.exceptions.CustomException;
import org.univaq.swa.exceptions.RESTWebApplicationException;
import org.univaq.swa.framework.security.DBConnection;
import org.univaq.swa.framework.security.Logged;
import org.univaq.swa.model.Classroom;

/**
 *
 * @author Ago95Dev
 */

 @Path("event")
public class EventResource {
     
    private static Connection con;
    
    public EventResource() throws SQLException, CustomException {
        con = DBConnection.getConnection();
    }
    
    /*  public Response addEvent(@Context UriInfo uriinfo, String json, @Context SecurityContext securityContext) {
        String addEvent = "INSERT INTO event (id,`name`,`date`,`start_time`,`end_time`,`description`,`type`,`event_cordinator_id`,`course_id`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        //String addCoordinator = 
        //String addRecurrent = 
        //String addEventAsRecurrent = 
        }
    */
}
