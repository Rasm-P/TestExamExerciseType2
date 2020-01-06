package rest;

import entities.Category;
import entities.Role;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@Disabled
public class ResourceTest {

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
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    User user = new User("user", "test");
    User admin = new User("admin", "test");
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    Category Career = new Category("Career");
    Category celebrity = new Category("celebrity");
    Category dev = new Category("dev");
    Category explicit = new Category("explicit");
    Category fashion = new Category("fashion");
    Category food = new Category("food");
    Category history = new Category("history");
    Category money = new Category("money");
    Category movie = new Category("movie");
    Category music = new Category("music");
    Category political = new Category("political");
    Category science = new Category("science");
    Category sport = new Category("sport");
    Category travel = new Category("travel");

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            user.addRole(userRole);
            admin.addRole(adminRole);

            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Category").executeUpdate();

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);

            em.persist(Career);
            em.persist(celebrity);
            em.persist(dev);
            em.persist(explicit);
            em.persist(fashion);
            em.persist(food);
            em.persist(history);
            em.persist(money);
            em.persist(movie);
            em.persist(music);
            em.persist(political);
            em.persist(science);
            em.persist(sport);
            em.persist(travel);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testCategoryCount() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/categoryCount/sport").then()
                .statusCode(200)
                .body("count", equalTo(0));
    }

    @Test
    public void testJokeByCategoryNotOverFour() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/jokeByCategory/1,2,3,4,5").then()
                .statusCode(500)
                .body("message", equalTo("For this request, a maximum of 4 categories is allowed!"));
    }

    @Test
    public void testJokeByCategoryV2NotOverTwelve() {
        login("user", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/jokeByCategoryV2/1,2,3,4,5,6,7,8,9,10,11,12,13").then()
                .statusCode(500)
                .body("message", equalTo("For this request, a maximum of 12 categories is allowed!"));
    }

    @Test
    public void testAddNewCategory() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .body("{\"category\": \"string\",\n"
                        + "  \"requestList\": []}")
                .when()
                .post("/newCategory")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteNewCategory() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/newCategory/" + travel.getId())
                .then()
                .statusCode(200);
    }

}
