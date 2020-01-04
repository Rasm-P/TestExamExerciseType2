package facades;

import entities.Category;
import entities.Request;
import utils.EMF_Creator;
import entities.Role;
import entities.User;
import errorhandling.AuthenticationException;
import errorhandling.CategoryException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//@Disabled
public class FacadeTest {

    private static EntityManagerFactory emf;
    private static EntityFacade facade;
    private static UserFacade userFacade;

    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = EntityFacade.getFacade(emf);
        userFacade = UserFacade.getUserFacade(emf);
    }

    User user = new User("user", "sdfsd");
    User admin = new User("admin", "tessdt");
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testFindLegalCategroy() {
        String expected = "food";
        assertEquals(expected, facade.findLegalCategroy("food").getCategory());
    }

    @Test
    public void testAddRequest() {
        Request r1 = new Request();
        Request r2 = facade.addRequest(r1);
        assertEquals(r1.getId(), r2.getId());
    }

    @Test
    public void testCategoryUpdate() {
        Category c1 = facade.findLegalCategroy("food");
        c1.setCategory("numbers");
        Category c2 = facade.categoryUpdate(c1);
        assertEquals(c2.getCategory(), "numbers");
    }

    @Test
    public void testAddCategroy() {
        Category c1 = new Category("letters");
        Category c2 = facade.addCategroy(c1);
        assertEquals(c2.getId(), c1.getId());
    }

    @Test
    public void testRemoveCategroy() {
        try {
            Category c = facade.removeCategroy(facade.findLegalCategroy("food").getId());
        } catch (CategoryException ex) {
            Assert.isTrue(true, "Category does not exist!");
        }
    }

    @Test
    public void testGetVeryfiedUser() throws AuthenticationException {
        User u = userFacade.getVeryfiedUser(user.getUserName(), "sdfsd");
        assertEquals(u.getUserName(), user.getUserName());
    }
}
