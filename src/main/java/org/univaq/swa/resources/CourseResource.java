package org.univaq.swa.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.univaq.swa.exceptions.CustomException;
import org.univaq.swa.exceptions.RESTWebApplicationException;
import org.univaq.swa.framework.security.DBConnection;

/**
 *
 * @author gianlucarea
 */
@Path("course")
public class CourseResource {

    private static Connection con;

    public CourseResource() throws SQLException, CustomException {
        con = DBConnection.getConnection();
    }

    /**
     * Get all classrooms
     *
     * @param uriinfo
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllClassroom(@Context UriInfo uriinfo) {
        String getCourseQuery = "SELECT id,name FROM course;";

        Map<String, Object> responseMap = new LinkedHashMap<>();
        try {
            PreparedStatement getCoursePS = con.prepareStatement(getCourseQuery);
            ResultSet rs = getCoursePS.executeQuery();

            while (rs.next()) {
                responseMap.put(rs.getString("name"), rs.getInt("id"));
            }

            if (responseMap.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(responseMap).build();
        } catch (SQLException ex) {
            Logger.getLogger(CourseResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
    }

 
}
