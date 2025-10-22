package dao;

import java.util.Collection;
import java.util.List;

import dao.generic.GenericDAO;
import domain.Client;
import domain.Product;
import domain.ProductQuantity;
import domain.Sale;
import domain.Sale.Status;
import exceptions.DAOException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class SaleDAO extends GenericDAO<Sale, Long> implements ISaleDAO{

    public SaleDAO(EntityManagerFactory entityManagerFactory) {
        super(Sale.class, entityManagerFactory);
    }

    @Override
    public Boolean cancelSale(Sale sale) throws DAOException {
        sale.setStatus(Status.CANCELADA);
        Boolean cancel = super.update(sale);
        return cancel;
    }

    @Override
    public Boolean completeSale(Sale sale) throws DAOException {
        sale.setStatus(Status.CONCLUIDA);
        Boolean complete = super.update(sale);
        return complete;
    }

    @Override
    public Boolean create(Sale sale) throws DAOException {
        try {
            openConnection();
            for (ProductQuantity productQuantity : sale.getProductList()) {
                Product p = entityManager.merge(productQuantity.getProduct());
                productQuantity.setProduct(p);
            }
            Client client = entityManager.merge(sale.getClient());
            sale.setClient(client);
            entityManager.persist(sale);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO CADASTRAR VENDA", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Sale searchWithCollection(Long saleId) throws DAOException {
        try {
            openConnection();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Sale> query = builder.createQuery(Sale.class);
            Root<Sale> root = query.from(Sale.class);
            root.fetch("client");
            root.fetch("productList");
            query.select(root).where(builder.equal(root.get("id"), saleId));
            TypedQuery<Sale> typedQuery = entityManager.createQuery(query);
            Sale sale = typedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return sale;
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DAOException("ERRO AO PROCURAR VENDA", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Sale searchByCode(String code) throws DAOException {
        try {
            openConnection();
            String jpql = "SELECT s FROM Sale s WHERE s.code = :code";
            Sale sale = entityManager
                .createQuery(jpql, Sale.class)
                .setParameter("code", code)
                .getSingleResult();
            entityManager.getTransaction().commit();
            return sale;
        } catch (Exception e) {
            throw new DAOException("ERRO AO BUSCAR VENDA PELO CÓDIGO", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Collection<Sale> all() throws DAOException {
        try {
            openConnection();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Sale> query = builder.createQuery(Sale.class);
            Root<Sale> root = query.from(Sale.class);
            root.fetch("client", JoinType.LEFT);
            Fetch<Sale, ProductQuantity> productListFetch = root.fetch("productList", JoinType.LEFT);
            if (productListFetch instanceof Join<?, ?> join) {
                join.fetch("product", JoinType.LEFT);
            }
            query.select(root).distinct(true);
            List<Sale> result = entityManager.createQuery(query).getResultList();
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            throw new DAOException("ERRO AO MOSTRAR TODAS AS VENDA", e);
        } finally {
            closeConnection();
        }
    }
}
