package rest;

import dto.ResponceDTO;
import errorhandling.CategoryException;
import facades.JokeFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import java.util.concurrent.ExecutionException;

@Path("jokeByCategory")
public class JokeByCategoryResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final JokeFacade j = JokeFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"JokeByCategory\"}";
    }
    
    @GET
    @Path("/{categories}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponceDTO jokeByCategory(@PathParam("categories") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        String[] categoryArray = jsonString.split(",");
        if (categoryArray.length > 4) {
            throw new CategoryException("For this request, a maximum of 4 categories is allowed!");
        } else {
        return j.jokes(categoryArray);
        }
    }
    
}
