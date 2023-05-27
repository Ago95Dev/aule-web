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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * @author gianlucarea
 */
@Path("classroom")
public class ClassroomResource {
    
    private static Connection con ;
    
    public ClassroomResource() throws SQLException, CustomException {
        con = DBConnection.getConnection();
    }
    
    static Map<String,Object> createClassroom(ResultSet rs){
        try{
            Map<String,Object> classroom = new LinkedHashMap<>();
            classroom.put("id", rs.getInt("id"));
            classroom.put("name", rs.getString("name"));
            classroom.put("positionID", rs.getInt("position_id"));
            classroom.put("capacity", rs.getInt("capacity"));
            classroom.put("email", rs.getString("email"));
            classroom.put("numberOfEthernet", rs.getInt("number_ethernet"));
            classroom.put("numberOfSockets", rs.getInt("number_socket"));
            classroom.put("note", rs.getString("note")); 
            return classroom;
        } catch (SQLException ex) {
            throw new RESTWebApplicationException(ex);
        }
    }
    
    @POST
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addClassroom")
    public Response addClassroom(@Context UriInfo uriinfo,
                            //un altro modo per ricevere e iniettare i parametri con JAX-RS...
                            String json,
                            @Context SecurityContext securityContext) {
        String addClassQuery = "INSERT INTO classroom (name, position_id, capacity, email, number_socket, number_ethernet, note) VALUES (?, ?, ?, ?, ?, ?, ?);";
        String addEquipment = "INSERT INTO classroom_has_equipment(classroom_id,equipment_id) VALUES(?,?);";
        Classroom classroom = new Classroom();
        
        try{
            classroom =new ObjectMapper().readValue(json, Classroom.class);
        } catch(JsonProcessingException e){
            e.printStackTrace();
        } 

        try(PreparedStatement ps = con.prepareStatement(addClassQuery, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, classroom.getName() );
            ps.setInt(2, classroom.getPositionID());
            ps.setInt(3, classroom.getCapacity());
            ps.setString(4, classroom.getEmail());
            ps.setInt(5, classroom.getNumberOfSockets());
            ps.setInt(6, classroom.getNumberOfEthernet());
            ps.setString(7, classroom.getNote());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                rs.next();
                int id_classroom = rs.getInt(1);
                
                for(Integer equipmentId : classroom.getEquipmentsId()){
                    try (PreparedStatement ps2 = con.prepareStatement(addEquipment)) {
                        ps2.setInt(1, id_classroom);
                        ps2.setInt(2, equipmentId);
                        ps2.executeUpdate();
                    }
                }
            URI uri = uriinfo.getBaseUriBuilder().path("classroom/" + id_classroom ).build();
            return Response.created(uri).build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @PUT
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateClassroom/{classroom_id}")
    public Response updateClassroom(@Context UriInfo uriinfo,
                            //un altro modo per ricevere e iniettare i parametri con JAX-RS...
                            @PathParam("classroom_id") Integer classroom_id,
                            String json,
                            @Context SecurityContext securityContext) {
        String updateClassQuery = "UPDATE classroom SET name = ?, position_id = ?, capacity = ?, email = ?, number_socket =?, number_ethernet = ? , note =? WHERE id = ?;";
        String addEquipment = "INSERT INTO classroom_has_equipment(classroom_id,equipment_id) VALUES(?,?);";
        String cleanEquipment = "DELETE FROM classroom_has_equipment WHERE classroom_id = ?;";
        System.out.println("Qui modifica");
        Classroom classroom = new Classroom();
        
        try{
            classroom =new ObjectMapper().readValue(json, Classroom.class);
        } catch(JsonProcessingException e){
            e.printStackTrace();
        } 

        try(PreparedStatement ps = con.prepareStatement(updateClassQuery)){
            ps.setString(1, classroom.getName() );
            ps.setInt(2, classroom.getPositionID());
            ps.setInt(3, classroom.getCapacity());
            ps.setString(4, classroom.getEmail());
            ps.setInt(5, classroom.getNumberOfSockets());
            ps.setInt(6, classroom.getNumberOfEthernet());
            ps.setString(7, classroom.getNote());
            ps.setInt(8,classroom_id);
            ps.executeUpdate();

            try {
                PreparedStatement ps2 = con.prepareStatement(cleanEquipment);
                ps2.setInt(1, classroom_id);
                ps2.execute();
                        
                for(Integer equipmentId : classroom.getEquipmentsId()){
                    try (PreparedStatement ps3 = con.prepareStatement(addEquipment)) {
                        ps3.setInt(1, classroom_id);
                        ps3.setInt(2, equipmentId);
                        ps3.executeUpdate();
                    }
                }
            URI uri = uriinfo.getBaseUriBuilder().path("classroom/" + classroom_id ).build();
            return Response.created(uri).build();
            }catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
