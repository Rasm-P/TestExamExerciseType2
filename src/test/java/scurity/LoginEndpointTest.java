/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scurity;

import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import rest.ApplicationConfig;
import utils.EMF_Creator;

/**
 *
 * @author Rasmus2
 */
//@Disabled
public class LoginEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testLoginAdmin() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"username\":\"admin\", \"password\":\"test\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("username", equalTo("admin"));
    }

    @Test
    public void testLoginUser() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"username\":\"user\", \"password\":\"test\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("username", equalTo("user"));
    }

    @Test
    public void testNoLogin1() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"username\":\"user123\", \"password\":\"te123123st\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(403)
                .body("message", equalTo("Invalid user name or password"));
    }

    @Test
    public void testNoLogin2() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"username\":\"user\", \"password\":\"te123123st\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(403)
                .body("message", equalTo("Invalid user name or password"));
    }

    @Test
    public void testNoLogin3() throws Exception {
        given()
                .contentType("application/json")
                .body("{\"username\":\"user123\", \"password\":\"test\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(403)
                .body("message", equalTo("Invalid user name or password"));
    }

}
