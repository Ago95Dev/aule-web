package org.univaq.swa.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import org.univaq.swa.exceptions.CustomException;
import org.univaq.swa.exceptions.RESTWebApplicationException;
import org.univaq.swa.framework.security.DBConnection;
import org.univaq.swa.framework.security.Logged;
import org.univaq.swa.model.Classroom;
import org.univaq.swa.model.Group;
import org.univaq.swa.model.Position;



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
            if(rs.next()){
                classroom.put("id", rs.getInt("id"));
                classroom.put("name", rs.getString("name"));
                classroom.put("positionID", rs.getInt("position_id"));
                classroom.put("capacity", rs.getInt("capacity"));
                classroom.put("email", rs.getString("email"));
                classroom.put("numberOfSockets", rs.getInt("number_socket"));
                classroom.put("numberOfEthernet", rs.getInt("number_ethernet"));
                classroom.put("note", rs.getString("note")); 
            }
            return classroom;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RESTWebApplicationException(ex);
        }
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{classroom_id}")
    public Response getClassroom(@Context UriInfo uriinfo, @PathParam("classroom_id") Integer classroom_id, SecurityContext securityContext){
        String getClassQuery = "SELECT * FROM classroom WHERE id = ?;";
        String getPositionQuery = "SELECT * FROM position WHERE id=?;";
        String getEquipmentIdsQuery = "SELECT equipment_id FROM classroom_has_equipment WHERE classroom_id = ?;";
        String getGroupIdsQuery = "SELECT group_id FROM group_has_classroom WHERE classroom_id = ?;";
        String getGroupQuery = "SELECT * FROM `group` WHERE id=?;";
        String getEquipmentQuery = "SELECT * FROM equipment WHERE id=?;";
        
        Map<String, Object> responseMap = new LinkedHashMap<>();
        boolean flag = false;
        try(PreparedStatement getClassPS = con.prepareStatement(getClassQuery)){
            getClassPS.setInt(1,classroom_id);
            ResultSet rs = getClassPS.executeQuery();
            responseMap = createClassroom(rs);
            flag = true;
            try(PreparedStatement getPositionPS = con.prepareStatement(getPositionQuery)){
                getPositionPS.setInt(1, (int) responseMap.get("positionID"));
                ResultSet rs1 = getPositionPS.executeQuery();
                if(rs1.next()){
                    responseMap.put("location", rs1.getString("location"));
                    responseMap.put("building", rs1.getString("building"));
                    responseMap.put("floor", rs1.getString("floor"));
                }
            }
            
            try(PreparedStatement getEquipmentIDsPS = con.prepareStatement(getEquipmentIdsQuery)){
                getEquipmentIDsPS.setInt(1, classroom_id);
                ResultSet rs2 = getEquipmentIDsPS.executeQuery();
                ArrayList<Integer> equipmentIds = new ArrayList<Integer>();
                ArrayList<String> equipmentName = new ArrayList<String>();
                while(rs2.next()){
                    equipmentIds.add(rs2.getInt("equipment_id"));
                }
                
                for(Integer equipmentId : equipmentIds){
                    try(PreparedStatement getEquipmentPS = con.prepareStatement(getEquipmentQuery)){
                        getEquipmentPS.setInt(1, equipmentId);
                        ResultSet rs3 = getEquipmentPS.executeQuery();
                        if(rs3.next()){
                            equipmentName.add(rs3.getString("name"));
                        }
                    }
                }
                
                responseMap.put("Equipments", equipmentName);
            }
            
            try(PreparedStatement getGroupIDsPS = con.prepareStatement(getGroupIdsQuery)){
                getGroupIDsPS.setInt(1, classroom_id);
                ResultSet rs4 = getGroupIDsPS.executeQuery();
                ArrayList<Group> groupOfClassroom = new ArrayList<>();
                ArrayList<Integer> groupIds = new ArrayList<Integer>();
                while(rs4.next()){
                    groupIds.add(rs4.getInt("group_id"));
                }
                for(Integer groupID : groupIds){
                    try(PreparedStatement getGroupPS = con.prepareStatement(getGroupQuery)){
                        getGroupPS.setInt(1, groupID);
                        ResultSet rs5 = getGroupPS.executeQuery();
                        if(rs5.next()){
                            Group group = new Group();
                            group.setName(rs5.getString("name"));
                            group.setDescription(rs5.getString("description"));
                            groupOfClassroom.add(group);
                        }
                    }
                }
                responseMap.put("Group", groupOfClassroom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        
        if(flag){
            return Response.ok(responseMap).build();
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all classrooms
     * @param uriinfo
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllClassroom(@Context UriInfo uriinfo){
        String getClassQuery = "SELECT id,name FROM classroom;";
        
        Map<String, Object> responseMap = new LinkedHashMap<>();
        boolean flag = false;
        try(PreparedStatement getClassPS = con.prepareStatement(getClassQuery)){
            flag = true;
            ResultSet rs = getClassPS.executeQuery();
            
            while(rs.next()){
            responseMap.put(rs.getString("name"), rs.getInt("id") );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        
        if(flag){
            return Response.ok(responseMap).build();
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
     @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/group/all")
    public Response getAllGroup(@Context UriInfo uriinfo){
        String getClassQuery = "SELECT id,name FROM `group`;";
        
        Map<String, Object> responseMap = new LinkedHashMap<>();
        boolean flag = false;
        try(PreparedStatement getClassPS = con.prepareStatement(getClassQuery)){
            flag = true;
            ResultSet rs = getClassPS.executeQuery();
            
            while(rs.next()){
            responseMap.put(rs.getString("name"), rs.getInt("id") );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        
        if(flag){
            return Response.ok(responseMap).build();
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    /**
     * Insert new classroom and manage equipment
     * @param uriinfo
     * @param json Collection of field to create new classroom
     * @param securityContext
     * @return 
     */
    @POST
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addClassroom")
    public Response addClassroom(@Context UriInfo uriinfo,String json,@Context SecurityContext securityContext) {
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
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
    }
    
    /**
     *  Update classroom
     * @param uriinfo
     * @param classroom_id id of classroom to update
     * @param json collection of field to update
     * @param securityContext
     * @return 
     */
    
    @PUT
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateClassroom/{classroom_id}")
    public Response updateClassroom(@Context UriInfo uriinfo,
                            @PathParam("classroom_id") Integer classroom_id,
                            String json,
                            @Context SecurityContext securityContext) {
        String updateClassQuery = "UPDATE classroom SET name = ?, position_id = ?, capacity = ?, email = ?, number_socket =?, number_ethernet = ? , note =? WHERE id = ?;";
        String addEquipment = "INSERT INTO classroom_has_equipment(classroom_id,equipment_id) VALUES(?,?);";
        String cleanEquipment = "DELETE FROM classroom_has_equipment WHERE classroom_id = ?;";
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
                Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        return null;
    }
    
    
    
    /**
     * Insert new classroom into a group
     * @param uriinfo
     * @param classroom_id 
     * @param group_id
     * @param securityContext 
     * @return 
     */
    @POST
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{classroom_id}/group/{group_id}")
    public Response addClassroom(@Context UriInfo uriinfo,
                            @PathParam("classroom_id") Integer classroom_id,
                            @PathParam("group_id") Integer group_id,
                            @Context SecurityContext securityContext) {
        String addClassToGroupQuery = "INSERT INTO group_has_classroom (group_id, classroom_id) VALUES (?, ?);";

        try(PreparedStatement ps = con.prepareStatement(addClassToGroupQuery)){
            ps.setInt(1, group_id);
            ps.setInt(2, classroom_id);
            ps.executeUpdate(); 
            URI uri = uriinfo.getBaseUriBuilder().path("classroom/" + classroom_id ).build();
            return Response.created(uri).build();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
    }
    
    @POST
    @Logged
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/csv/importCsv")
    public Response importCsv(@Context UriInfo uriinfo,
                    @FormDataParam("csvInputFile") InputStream fileInputStream,
                    @FormDataParam("csvInputFile") FormDataContentDisposition fileMetaData ,
                    @Context SecurityContext securityContext) {
        
        String insertClassroomQuery = "INSERT INTO classroom (name, position_id, capacity, email, number_socket, number_ethernet, note) VALUES (?, ?, ?, ?, ?, ?, ?);";
        String selectPositionIDQuery = "SELECT id FROM position WHERE location = ? AND building = ? AND floor =?;";
        int position_id = 0;
        try{   
            CSVReader reader = new CSVReader(new InputStreamReader(fileInputStream, "UTF-8"));
           String[] record = null;
           while((record = reader.readNext()) != null){
               System.out.println(record[0]);
                try(PreparedStatement ps1 = con.prepareStatement(selectPositionIDQuery)){
                    ps1.setString(1, record[1] );
                    ps1.setString(2, record[2] );
                    ps1.setString(3, record[3] );
                    ResultSet rs2 = ps1.executeQuery();
                    while(rs2.next()){
                        position_id = rs2.getInt("id");
                    }                
                }
                try(PreparedStatement ps2 = con.prepareStatement(insertClassroomQuery)){
                    ps2.setString(1, record[0]);
                    ps2.setInt(2, position_id);
                    ps2.setInt(3, Integer.parseInt(record[4]));
                    ps2.setString(4, record[5]);
                    ps2.setInt(5, Integer.parseInt(record[6]));
                    ps2.setInt(6, Integer.parseInt(record[7]));
                    ps2.setString(7, record[8]);
                    ps2.executeUpdate();
                }
           }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        } catch (IOException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.accepted().build();
    }
    
    
    @GET
    @Logged
    @Produces("text/csv")
    @Path("/csv/export")
    public Response exportCsv(@Context UriInfo uriinfo,
                    @Context SecurityContext securityContext) {
        
        String getClassroomQuery = "SELECT * FROM classroom ";
        String selectPositionIDQuery = "SELECT * FROM position WHERE id = ?";
        File file = new File("untitled.csv");
        try{
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile , ',', CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
            ArrayList<Classroom> classrooms = new ArrayList<Classroom>();
            String[] entries = new String[9];
            try(PreparedStatement ps1 = con.prepareStatement(getClassroomQuery)){
                    ResultSet rs1 = ps1.executeQuery();
                    while(rs1.next()){
                        Classroom tClassroom= new Classroom();
                        tClassroom.setId(rs1.getInt("id"));
                        tClassroom.setName(rs1.getString("name"));
                        tClassroom.setPositionID(rs1.getInt("position_id"));
                        tClassroom.setCapacity(rs1.getInt("capacity"));
                        tClassroom.setEmail(rs1.getString("email"));
                        tClassroom.setNumberOfSockets(rs1.getInt("number_socket"));
                        tClassroom.setNumberOfEthernet(rs1.getInt("number_ethernet"));
                        tClassroom.setNote(rs1.getString("note"));
                        classrooms.add(tClassroom);
                }                
            }
            
            for(Classroom classroom : classrooms){
                Position position = new Position();
                try(PreparedStatement ps2 = con.prepareStatement(selectPositionIDQuery)){
                    ps2.setInt(1, classroom.getPositionID());
                    ResultSet rs2 = ps2.executeQuery();
                    if(rs2.next()){
                       position.setId(classroom.getPositionID());
                       position.setBuilding(rs2.getString("building"));
                       position.setLocation(rs2.getString("location"));
                       position.setFloor(rs2.getString("floor"));
                    }                
                }
                entries[0] = classroom.getName();
                entries[1] = position.getLocation();
                entries[2] = position.getBuilding();
                entries[3] = position.getFloor();
                entries[4] = String.valueOf(classroom.getCapacity());
                entries[5] = classroom.getEmail();
                entries[6] = String.valueOf(classroom.getNumberOfSockets());
                entries[7] = String.valueOf(classroom.getNumberOfEthernet());
                entries[8] = classroom.getNote();
                writer.writeNext(entries);
            }
            writer.close();
            return Response.ok(file).build();
        } catch (IOException e){
            e.printStackTrace();
        }  catch (SQLException ex) {
            Logger.getLogger(ClassroomResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
