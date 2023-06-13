package org.univaq.swa.resources;

import jakarta.json.Json;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.univaq.swa.exceptions.CustomException;
import org.univaq.swa.exceptions.RESTWebApplicationException;
import org.univaq.swa.framework.security.DBConnection;
import org.univaq.swa.model.Event;
import org.univaq.swa.model.Type;
import org.json.JSONObject;

/**
 *
 * @author Ago95Dev,gianlucarea
 */
@Path("event")
public class EventResource {

    private static Connection con;

    public EventResource() throws SQLException, CustomException {
        con = DBConnection.getConnection();
    }

    private Map<String, Object> createEvent(ResultSet rs) {
        try {
            Map<String, Object> event = new LinkedHashMap<>();
            if (rs.next()) {
                event.put("id", rs.getInt("id"));
                event.put("name", rs.getString("name"));
                event.put("date", rs.getDate("date").toString());
                event.put("start_time", rs.getTime("start_time"));
                event.put("end_time", rs.getTime("end_time"));
                event.put("description", rs.getString("description"));
                event.put("type", rs.getString("type"));
                event.put("email", rs.getString("email"));
                if (rs.getInt("course_id") > 0) {
                    event.put("course_id", rs.getInt("course_id"));
                } else {
                    event.put("course_id", null);
                }
                event.put("classroom_id", rs.getInt("classroom_id"));
            }
            return event;
        } catch (SQLException ex) {
            throw new RESTWebApplicationException(ex);
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{event_id}")
    public Response getEvent(@Context UriInfo uriinfo, @PathParam("event_id") Integer event_id) {

        String getEventQuery = "SELECT * FROM event WHERE id = ?;";
        String getClassNameQuery = "SELECT name FROM classroom WHERE id=?;";
        String getCourseNameQuery = "SELECT name FROM course WHERE id=?";

        Map<String, Object> responseMap = new LinkedHashMap<>();

        try ( PreparedStatement ps = con.prepareStatement(getEventQuery)) {
            ps.setInt(1, event_id);
            ResultSet rs = ps.executeQuery();
            responseMap = createEvent(rs);
            try ( PreparedStatement ps2 = con.prepareStatement(getClassNameQuery)) {
                ps2.setInt(1, (int) responseMap.get("classroom_id"));
                ResultSet rs1 = ps2.executeQuery();
                if (rs1.next()) {
                    responseMap.put("classroom", rs1.getString("name"));
                }
            }
            if (responseMap.get("course_id") != null) {
                try ( PreparedStatement ps3 = con.prepareStatement(getCourseNameQuery)) {
                    ps3.setInt(1, (int) responseMap.get("course_id"));
                    ResultSet rs1 = ps3.executeQuery();
                    if (rs1.next()) {
                        responseMap.put("course", rs1.getString("name"));
                    }
                }
            }
            if (responseMap.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(responseMap).build();

        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
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
        String clasroomStringID = (String) event.get("classroom_id");
        LocalDate startDate = LocalDate.parse(firstOccurenceDate);
        String courseIdFromJSON = (String) event.get("course_id");

        int classroom_id = Integer.parseInt(clasroomStringID);
        int course_id = 0;
        if (courseIdFromJSON != null) {
            course_id = Integer.parseInt((String) event.get("course_id"));
        }
        if (event.get("until_date") != "") {
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

                        ps2.setTime(3, Time.valueOf(startTime + ":00"));
                        ps2.setTime(4, Time.valueOf(endTime + ":00"));
                        ps2.setString(5, description);
                        ps2.setString(6, type);
                        ps2.setString(7, email);
                        if (course_id != 0) {
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
                    if (eventID != 0) {
                        try ( PreparedStatement ps3 = con.prepareStatement(addEventAsRecurrent)) {
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
                ps4.setTime(3, Time.valueOf(startTime + ":00"));
                ps4.setTime(4, Time.valueOf(endTime + ":00"));
                ps4.setString(5, description);
                ps4.setString(6, type);
                ps4.setString(7, email);
                if (course_id != 0) {
                    ps4.setInt(8, course_id);
                } else {
                    ps4.setNull(8, java.sql.Types.INTEGER);
                }
                ps4.setInt(9, classroom_id);
                ps4.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(EventResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.accepted().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/now")
    public Response getNowEvents(@Context UriInfo uriinfo) {

        LocalDate today = LocalDate.now();
        String getNowEventsQuery = "SELECT * FROM event WHERE date = ?;";
        String getCourse = "SELECT name FROM course WHERE id= ?";
        String getClassNameQuery = "SELECT name FROM classroom WHERE id=?;";
        Map<String, Map<String, Object>> responseMap = new LinkedHashMap<>();
        try {
            PreparedStatement ps = con.prepareStatement(getNowEventsQuery);
            ps.setDate(1, Date.valueOf(today));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalTime start = (LocalTime) rs.getTime("start_time").toLocalTime();
                LocalTime end = (LocalTime) rs.getTime("end_time").toLocalTime();
                LocalTime now = LocalTime.now();
                if ((start.isBefore(now) && end.isAfter(now))
                        || (start.isAfter(now) && start.isBefore(now.plusHours(3)))) {

                    Map<String, Object> x = new LinkedHashMap<>();
                    String id = String.valueOf(rs.getInt("id"));
                    x.put("id", rs.getInt("id"));
                    x.put("name", rs.getString("name"));
                    x.put("description", rs.getString("description"));
                    x.put("date", today.toString());
                    x.put("start_time", start.toString());
                    x.put("end_time", end.toString());
                    x.put("email", rs.getString("email"));
                    x.put("type", rs.getString("type"));
                    x.put("classroom_id", rs.getInt("classroom_id"));
                    try ( PreparedStatement ps2 = con.prepareStatement(getClassNameQuery)) {
                        ps2.setInt(1, rs.getInt("classroom_id"));
                        ResultSet rs1 = ps2.executeQuery();
                        if (rs1.next()) {
                            x.put("classroom", rs1.getString("name"));
                        }
                    }

                    if (rs.getInt("course_id") >= 0) {
                        try ( PreparedStatement ps1 = con.prepareStatement(getCourse)) {
                            ps1.setInt(1, rs.getInt("course_id"));
                            ResultSet rs1 = ps1.executeQuery();
                            if (rs1.next()) {
                                x.put("course", rs1.getString("name"));
                            }
                        }
                    }

                    responseMap.put(id, x);
                }
            }

            if (responseMap.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(responseMap).build();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
    }

    //WARNING terminale: GET ClassroomResource.getClassroom, should not consume any entity.
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/week")
    public Response getWeekEvents(@Context UriInfo uriinfo, Map<String, Object> info) {

        String getNowEventsQuery = "SELECT * FROM event WHERE date BETWEEN ? AND ?;";
        String getCourse = "SELECT name FROM course WHERE id= ?";
        String getClassNameQuery = "SELECT name FROM classroom WHERE id=?;";
        LocalDate choosenDate = LocalDate.parse(info.get("date_choosen").toString());
        LocalDate start = YearMonth.of(choosenDate.getYear(), choosenDate.getMonth()).atDay(choosenDate.getDayOfMonth()).with(DayOfWeek.MONDAY);
        LocalDate end = YearMonth.of(choosenDate.getYear(), choosenDate.getMonth()).atDay(choosenDate.getDayOfMonth()).with(DayOfWeek.SUNDAY);

        Map<String, Map<String, Object>> responseMap = new LinkedHashMap<>();
        try {
            PreparedStatement ps = con.prepareStatement(getNowEventsQuery);
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Map<String, Object> x = new LinkedHashMap<>();
                System.out.println(rs.getDate("date"));
                String id = String.valueOf(rs.getInt("id"));
                x.put("id", rs.getInt("id"));
                x.put("name", rs.getString("name"));
                x.put("description", rs.getString("description"));
                x.put("date", rs.getDate("date").toString());
                x.put("start_time", rs.getTime("start_time"));
                x.put("end_time", rs.getTime("end_time"));
                x.put("email", rs.getString("email"));
                x.put("type", rs.getString("type"));
                x.put("classroom_id", rs.getInt("classroom_id"));
                try ( PreparedStatement ps2 = con.prepareStatement(getClassNameQuery)) {
                    ps2.setInt(1, rs.getInt("classroom_id"));
                    ResultSet rs1 = ps2.executeQuery();
                    if (rs1.next()) {
                        x.put("classroom", rs1.getString("name"));
                    }
                }
                if (rs.getInt("course_id") >= 0) {
                    try ( PreparedStatement ps1 = con.prepareStatement(getCourse)) {
                        ps1.setInt(1, rs.getInt("course_id"));
                        ResultSet rs1 = ps1.executeQuery();
                        if (rs1.next()) {
                            x.put("course", rs1.getString("name"));
                        }
                    }

                    responseMap.put(id, x);
                }
            }

            if (responseMap.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(responseMap).build();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
    }
}
