/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.ResponceDTO;
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
@OpenAPIDefinition(
        info = @Info(
                title = "TestExamExerciseType2V2",
                version = "0.1",
                description = "Backend of TestExamExerciseType2V2"
        ),
        tags = {
            @Tag(name = "Jokes By Categories V2", description = "API for logged in user and admin roels to search for a maximum of 12 categories and get 12 jokes in return")
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
@Path("jokeByCategoryV2")
public class JokeByCategoryV2Resource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final JokeFacade j = JokeFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/{categories}")
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Allows for logged in admin and user roles to search for a maximum of 12 categories and get 12 jokes in return",
            tags = {"Jokes By Categories V2"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponceDTO.class))),
                @ApiResponse(responseCode = "200", description = "A joke for each searched for category is returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized"),
                @ApiResponse(responseCode = "500", description = "Too many or more than 12 requested categories")})
    public ResponceDTO jokeByCategoryV2(@PathParam("categories") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        String[] categoryArray = jsonString.split(",");
        if (categoryArray.length > 12) {
            throw new CategoryException("For this request, a maximum of 12 categories is allowed!");
        } else {
        return j.jokes(categoryArray);
        }
    }
}
