/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.CategoryDTO;
import entities.Category;
import errorhandling.CategoryException;
import facades.EntityFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
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
                title = "TestExamExerciseType2",
                version = "0.1",
                description = "Backend of TestExamExerciseType2"
        ),
        tags = {
            @Tag(name = "Category Count Endpoint", description = "API used for interaction with categories")
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
@Path("categoryCount")
public class CategoryCountResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final EntityFacade cateF = EntityFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get information about all legal categories",
            tags = {"Category Count Endpoint"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
                @ApiResponse(responseCode = "200", description = "Returns all categories"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = cateF.getAllCategories();
        List<CategoryDTO> dto = new ArrayList<>();
        for(Category c : categories){
            dto.add(new CategoryDTO(c));
        }
        return dto;
    }
    
    @GET
    @Path("/{category}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Allows admin roles to get a count of the total number of interations or a specified category",
            tags = {"Category Count Endpoint"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "200", description = "The total number of interactions with the spcifed category is returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public String categoryCount(@PathParam("category") String jsonString) throws InterruptedException, ExecutionException, CategoryException {
        Category category = cateF.findLegalCategroy(jsonString);
        return "{\"count\":"+category.getRequestList().size()+"}";
    }

}
