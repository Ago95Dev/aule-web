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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.univaq.swa.exceptions.CustomException;
import org.univaq.swa.exceptions.RESTWebApplicationException;
import org.univaq.swa.framework.security.DBConnection;
import org.univaq.swa.framework.security.Logged;
import org.univaq.swa.model.Classroom;
import org.univaq.swa.model.Event;
import org.univaq.swa.model.TypeOfRecurrency;

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
    @Path("/add")
    public Response addEvent(@Context UriInfo uriinfo, Map<String, Object> event, @Context SecurityContext securityContext) {
        String addEvent = "INSERT INTO event (`name`,`date`,`start_time`,`end_time`,`description`,`type`,`email`,`course_id`,`classroom_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?);";
        String addRecurrent = "INSERT INTO recurrent (`until_date`, `typeOfRecurrency`) VALUES (?, ?);";
        String addEventAsRecurrent = "INSERT INTO event_has_recurrent(recurrent_id, event_id) VALUES (?,?);";
        int recurrent_id = 0;

        //Common attribute -> Equal for each reccurent event or for the single event.
        String startTime = (String) event.get("start_time");
        String endTime = (String) event.get("end_time");
        String description = (String) event.get("description");
        String type = (String) event.get("type");
        String email = (String) event.get("email");
        String firstOccurenceDate = (String) event.get("date");
        LocalDate startDate = LocalDate.parse(firstOccurenceDate);

        int classroom_id = (int) event.get("classroom_id");
        int course_id = 0;
        if (event.containsKey("course_id")) {
            course_id = (int) event.get("course_id");
        }
        if (event.containsKey("until_date")) {
            try ( PreparedStatement ps1 = con.prepareStatement(addRecurrent, Statement.RETURN_GENERATED_KEYS)) {

                String dateAsString = (String) event.get("until_date");
                String tor = "DAILY";
                if (event.containsKey("typeOfRecurrency")) {
                    tor = (String) event.get("typeOfRecurrency");
                }
                LocalDate until_date = LocalDate.parse(dateAsString);
                ps1.setDate(1, Date.valueOf(until_date));
                ps1.setString(2, tor);
                ps1.executeUpdate();
                ResultSet rs1 = ps1.getGeneratedKeys();
                if (rs1.next()) {
                    recurrent_id = rs1.getInt(1);
                }

                Stream<LocalDate> daysUntil;
                switch (tor) {
                    case "WEEKLY":
                        daysUntil = startDate.datesUntil(until_date, Period.ofWeeks(1));
                        break;
                    case "MONTLY":
                        daysUntil = startDate.datesUntil(until_date, Period.ofMonths(1));
                        break;
                    case "DAILY":
                    default:
                        daysUntil = startDate.datesUntil(until_date, Period.ofDays(1));
                        break;
                }
                LocalDate[] dates = daysUntil.toArray(LocalDate[]::new);
                for (LocalDate date : dates) {
                    int eventID = 0;
                    try ( PreparedStatement ps2 = con.prepareStatement(addEvent, Statement.RETURN_GENERATED_KEYS)) {

                        ps2.setString(1, (String) event.get("name"));
                        ps2.setDate(2, Date.valueOf(date));
                        ps2.setTime(3, Time.valueOf(startTime));
                        ps2.setTime(4, Time.valueOf(endTime));
                        ps2.setString(5, description);
                        ps2.setString(6, type);
                        ps2.setString(7, email);
                        if(course_id != 0 ){
                            ps2.setInt(8, course_id);
                        } else {
                            ps2.setNull(8, java.sql.Types.INTEGER);
                        }
                        ps2.setInt(9, classroom_id);
                        ps2.executeUpdate();
                        ResultSet rs2 = ps2.getGeneratedKeys();
                        if (rs2.next()) {
                            eventID = rs2.getInt(1);
                        }
                    }
                    if(eventID != 0){
                        try(PreparedStatement ps3 = con.prepareStatement(addEventAsRecurrent)){
                            ps3.setInt(1, recurrent_id);
                            ps3.setInt(2, eventID);
                            ps3.executeUpdate();
                        }
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(EventResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try ( PreparedStatement ps4 = con.prepareStatement(addEvent)) {

                        ps4.setString(1, (String) event.get("name"));
                        ps4.setDate(2, Date.valueOf(startDate));
                        ps4.setTime(3, Time.valueOf(startTime));
                        ps4.setTime(4, Time.valueOf(endTime));
                        ps4.setString(5, description);
                        ps4.setString(6, type);
                        ps4.setString(7, email);
                        if(course_id != 0 ){
                            ps4.setInt(8, course_id);
                        } else {
                            ps4.setNull(8, java.sql.Types.INTEGER);
                        }
                        ps4.setInt(9, classroom_id);
                        ps4.executeUpdate();                           
                    } catch (SQLException ex) {
                Logger.getLogger(EventResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.accepted().build();
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
