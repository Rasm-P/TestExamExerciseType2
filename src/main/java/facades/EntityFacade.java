/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Category;
import entities.Request;
import errorhandling.CategoryException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Rasmus2
 */
public class EntityFacade {

    private static EntityFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private EntityFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static EntityFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new EntityFacade();
        }
        return instance;
    }

    public Category findLegalCategroy(String category) throws NoResultException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Category> query
                    = em.createQuery("Select c from Category c where c.category = :category", Category.class);
            query.setParameter("category", category);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new CategoryException("Category does not exist!");
        } finally {
            em.close();
        }

    }

    public Request addRequest(Request request) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
            return request;
        } finally {
            em.close();
        }
    }

    public Category categoryUpdate(Category category) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
            return category;
        } finally {
            em.close();
        }
    }
    
    public Category addCategroy(Category category) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
            return category;
        } finally {
            em.close();
        }
    }
    
    public Category removeCategroy(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Category o = em.find(Category.class, id);
            em.remove(em.merge(o));
            em.getTransaction().commit();
            return o;
        } finally {
            em.close();
        }
    }
    
}
