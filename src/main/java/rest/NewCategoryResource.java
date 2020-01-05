/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.CategoryDTO;
import entities.Category;
import facades.EntityFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
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
            @Tag(name = "New Category", description = "API admin roles to add and delete legal categories")
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
@Path("newCategory")
public class NewCategoryResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final EntityFacade cateF = EntityFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Allows admin roels to create new legal categories",
            tags = {"New Category"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A new legal category is created"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public CategoryDTO createCategory(Category c) {
        Category category = cateF.addCategroy(c);
        CategoryDTO dto = new CategoryDTO(category);
        return dto;
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Allows admin roels to delete legal categories",
            tags = {"New Category"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A legal category is deleted"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public CategoryDTO deleteCategory(@PathParam("id") Long id) {
        Category category = cateF.removeCategroy(id);
        CategoryDTO dto = new CategoryDTO(category);
        return dto;
    }
}
