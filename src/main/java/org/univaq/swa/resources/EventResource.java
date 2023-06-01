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
import org.univaq.swa.model.Event;

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
    
    @POST
    //@Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addEvent")
    public Response addEvent(@Context UriInfo uriinfo, String json, @Context SecurityContext securityContext) {
        String addEvent = "INSERT INTO event (id,`name`,`date`,`start_time`,`end_time`,`description`,`type`,`event_cordinator_id`,`course_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String addCoordinator = "INSERT INTO event_cordinator (id,`email`) VALUES (?, ?);";
        String addRecurrent = "INSERT INTO recurrent (`until_date`, `typeOfRecurrency`) VALUES (?, ?);";
        String addEventAsRecurrent = "INSERT INTO event_has_recurrent(recurrent_id, event_id) VALUES (?,?);";
    // '1', 'Riunione', '2023-05-03', '11:03:12', '18:03:12', 'Riunione', 'RIUNIONE', '1', '1'
    // Prima Coordinator e Course
        Event event = new Event(); 
        //prova statica della query 
        try(PreparedStatement ps = con.prepareStatement(addEvent, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, 1);
            ps.setString(2, "Riunione2");
            ps.setDate(3, java.sql.Date.valueOf("2023-05-03"));
            ps.setTime(4, java.sql.Time.valueOf("11:03:12"));
            ps.setTime(5, java.sql.Time.valueOf("18:03:12"));
            ps.setString(6, "Riunione2");
            ps.setString(7, "RIUNIONE");
            ps.setInt(8, 1);
            ps.setInt(9, 1);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
            rs.next();
            int id_event= rs.getInt(1);
            URI uri = uriinfo.getBaseUriBuilder().path("Event/" + id_event ).build();
            return Response.created(uri).build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RESTWebApplicationException(ex);
        }
    }
/* 
    @PUT
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateEvent/{event_id}")
   /* public Response updateClassroom(@Context UriInfo uriinfo,
                            @PathParam("event_id") Integer event_id,
                            String json,
                            @Context SecurityContext securityContext) {
        String updateClassQuery = "UPDATE event SET id = ?, name = ?, date = ?, start_time = ?, end_time =?, description = ? , type =? , event_cordinator_id=? , course_id=? WHERE id = ?;";            
      }
   */
    
}   