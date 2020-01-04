/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.Category;
import errorhandling.CategoryException;
import facades.CategoryFacade;
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
@Path("categoryCount")
public class CategoryCountResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final CategoryFacade cateF = CategoryFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello CategoryCountResource\"}";
    }

    @GET
    @Path("/{category}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public int categoryCount(@PathParam("category") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        Category category = cateF.findLegalCategroy(jsonString);
        return category.getRequestList().size();
    }

}
