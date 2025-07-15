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

    public StockDAO(String persistenceUnit) {
        super(Stock.class, persistenceUnit);
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
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO ADICIONAR STOCK", e);
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public Boolean delete(Long id) throws DAOException {
        try {
            openConnection();
            Stock stock = entityManager.find(Stock.class, id);
            if (stock == null) {
                return false;
            }
            Product product = stock.getProduct();
            if (product != null) {
                product.setStock(null);
                entityManager.merge(product);
            }
            entityManager.remove(stock);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO DELETAR STOCK", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Stock read(Long id) throws DAOException {
        try {
            openConnection();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
            Root<Stock> root = query.from(Stock.class);
            root.fetch("product", JoinType.INNER);
            query.select(root).where(cb.equal(root.get("id"), id));
            TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
            Stock stock = typedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return stock;
        } catch(Exception e) {
            throw new DAOException("ERRO AO LER STOCK", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Stock searchByProductCode(String code) throws DAOException {
        try {
            openConnection();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
            Root<Stock> root = query.from(Stock.class);
            Join<Stock, Product> productJoin = root.join("product");
            query.select(root).where(cb.equal(productJoin.get("code"), code));
            TypedQuery<Stock> typedQuery = entityManager.createQuery(query);
            Stock result = typedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return result;
        } catch(jakarta.persistence.NoResultException e) {
            return null;
        } catch(Exception e) {
            throw new DAOException("ERRO AO PROCURAR PRODUTO PELO CÓDIGO", e);
        } finally {
            closeConnection();
        }
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
