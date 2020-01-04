package rest;

import dto.ResponceDTO;
import entities.User;
import errorhandling.CategoryException;
import facades.JokeFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@OpenAPIDefinition(
        info = @Info(
                title = "TestExamExerciseType2",
                version = "0.1",
                description = "Backend of TestExamExerciseType2"
        ),
        tags = {
            @Tag(name = "Jokes By Categories", description = "API to search for a maximum of 4 categories and get 4 jokes in return")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/TestExamExerciseType2"
            ),
            @Server(
                    description = "Server API",
                    url = "https://barfodpraetorius.dk/TestExamExerciseType2"
            )

        }
)
@Path("jokeByCategory")
public class JokeByCategoryResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final JokeFacade j = JokeFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/{categories}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Allows for all users to search for a maximum of 4 categories and get 4 jokes in return",
            tags = {"Jokes By Categories"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponceDTO.class))),
                @ApiResponse(responseCode = "200", description = "A joke for each searched for category is returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized"),
                @ApiResponse(responseCode = "500", description = "Too many or more than 4 requested categories")})
    public ResponceDTO jokeByCategory(@PathParam("categories") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        String[] categoryArray = jsonString.split(",");
        if (categoryArray.length > 4) {
            throw new CategoryException("For this request, a maximum of 4 categories is allowed!");
        } else {
        return j.jokes(categoryArray);
        }
    }
    
}
