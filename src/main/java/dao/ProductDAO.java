package dao;

import dao.generic.GenericDAO;
import domain.Product;
import exceptions.DAOException;
import jakarta.persistence.EntityManagerFactory;

public class ProductDAO extends GenericDAO<Product, Long> implements IProductDAO {
    public ProductDAO(EntityManagerFactory entityManagerFactory) {
        super(Product.class, entityManagerFactory);
    }

    @Override
    public Product searchByCode(String code) throws DAOException {
        try {
            openConnection();
            String jpql = "SELECT p FROM Product p WHERE p.code = :code";
            Product product = entityManager
                .createQuery(jpql, Product.class)
                .setParameter("code", code)
                .getSingleResult();
            entityManager.getTransaction().commit();
            return product;
        } catch (Exception e) {
            throw new DAOException("ERRO AO BUSCAR PRODUTO PELO CÓDIGO", e);
        } finally {
            closeConnection();
        }
    }
}
