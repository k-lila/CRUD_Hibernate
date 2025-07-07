package dao;

import dao.generic.GenericDAO;
import domain.Product;
import domain.Stock;
import exceptions.DAOException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class StockDAO extends GenericDAO<Stock, Long> implements IStockDAO {

    public StockDAO() {
        super(Stock.class);
    }


@Override
public Boolean create(Stock stock) throws DAOException {
    try {
        openConnection();
        Product product = entityManager.find(Product.class, stock.getProduct().getId());
        stock.setProduct(product);
        product.setStock(stock);
        entityManager.persist(stock);
        entityManager.flush();
        entityManager.getTransaction().commit();
        closeConnection();
        return true;
    } catch (Exception e) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        throw new DAOException("ERRO AO ADICIONAR OBJETO", e);
    }
}

@Override
public Boolean delete(Long id) throws DAOException {
    try {
        System.out.println("AQUI1");
        openConnection();
        Stock stock = entityManager.find(Stock.class, id);
        System.out.println("AQUI2");
        if (stock == null) {
            closeConnection();
            return false;
        }
        System.out.println("AQUI3");
        Product product = stock.getProduct();
        if (product != null) {
            product.setStock(null);
            entityManager.merge(product);
        }
        System.out.println("AQUI4");

        entityManager.remove(stock);
        System.out.println("AQUI5");
        entityManager.getTransaction().commit();
        System.out.println("AQUI6");
        closeConnection();
        System.out.println("AQUI7");
        return true;
    } catch (Exception e) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        throw new DAOException("ERRO AO DELETAR STOCK", e);
    }
}

    @Override
    public Stock read(Long id) throws DAOException {
        openConnection();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
        Root<Stock> root = query.from(Stock.class);
        root.fetch("product", JoinType.INNER);
        query.select(root).where(cb.equal(root.get("id"), id));
        TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
        Stock stock = typedQuery.getSingleResult();
        entityManager.getTransaction().commit();
        closeConnection();
        return stock;
    }

    @Override
    public Stock searchByProductCode(String code) throws DAOException {
        openConnection();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
        Root<Stock> root = query.from(Stock.class);
        Join<Stock, Product> productJoin = root.join("product");
        query.select(root).where(cb.equal(productJoin.get("code"), code));
        TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
        Stock result = typedQuery.getSingleResult();
        entityManager.getTransaction().commit();
        closeConnection();
        return result;
    }

    @Override
    public Boolean verifyQuantity(String code, Integer quantity) throws DAOException {
        Stock stock = searchByProductCode(code);
        if (stock.getQuantity() >= quantity) {
            return true;
        } else {
            return false;
        }
    }

}
