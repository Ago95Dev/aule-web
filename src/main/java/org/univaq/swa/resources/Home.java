/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.exceptions.RESTWebApplicationException;

/**
 *
 * @author gianlucarea
 */
@Path("home")
public class Home {
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response home() throws RESTWebApplicationException {

        String Hello ="Hello World";

        return Response.ok(Hello).build();
    }
}
