package facades;

import entities.Category;
import utils.EMF_Creator;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class FacadeTest {

    private static EntityManagerFactory emf;
    private static CategoryFacade facade;

    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = CategoryFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {

    User user = new User("user", "sdfsd");
    User admin = new User("admin", "tessdt");
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user.addRole(userRole);
    admin.addRole(adminRole);
    
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
    
    
    em.getTransaction().begin();
    
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }
    
//    @Test
//    public void getByCity() {
//        String expected = "food";
//        assertEquals(expected, facade.isCategroyLegal("food"));
//    }

}
