/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.ResponceDTO;
import errorhandling.CategoryException;
import facades.JokeFacade;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Rasmus2
 */
@Path("jokeByCategoryV2")
public class JokeByCategoryV2Resource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final JokeFacade j = JokeFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"JokeByCategoryV2\"}";
    }
    
    @GET
    @Path("/{categories}")
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponceDTO jokeByCategoryV2(@PathParam("categories") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        String[] categoryArray = jsonString.split(",");
        if (categoryArray.length > 12) {
            throw new CategoryException("For this request, a maximum of 12 categories is allowed!");
        } else {
        return j.jokes(categoryArray);
        }
    }
}
