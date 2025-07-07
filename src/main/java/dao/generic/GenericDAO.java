package dao.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import dao.Persistent;
import exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T, E> {

    private Class<T> persistentClass;
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    public GenericDAO(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    private String getSelectSql() {
        return "SELECT obj FROM " + persistentClass.getSimpleName() + " obj";
    }

    protected void openConnection() {
        entityManagerFactory = Persistence.createEntityManagerFactory("crud_JPA");
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    protected void closeConnection() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Override
    public Boolean create(T entity) throws DAOException {
        try {
            openConnection();
            entityManager.persist(entity);
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
    public T read(E value) throws DAOException {
        try {
            openConnection();
            T entity = entityManager.find(persistentClass, value);
            entityManager.getTransaction().commit();
            closeConnection();
            return entity;
        } catch (Exception e) {
            throw new DAOException("ERRO AO CONSULTAR OBJETO", e);
        }
    }

    @Override
    public Boolean update(T entity) throws DAOException {
        try {
            openConnection();
            T toUpdate = entityManager.find(persistentClass, entity.getId());
            if (toUpdate == null) {
                closeConnection();
                return false;
            } else {
                entityManager.merge(entity);
                entityManager.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO ALTERAR OBJETO", e);
        }
    }

    @Override
    public Boolean delete(E value) throws DAOException {
        try {
            openConnection();
            T toDelete = entityManager.find(persistentClass, value);
            if (toDelete == null) {
                closeConnection();
                return false;
            } else {
                entityManager.remove(toDelete);
                entityManager.getTransaction().commit();
                closeConnection();
                return true;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO DELETAR OBJETO", e);
        }
    }

    @Override
    public Collection<T> showAll() throws DAOException {
        try {
            openConnection();
            List<T> all = entityManager.createQuery(getSelectSql(), persistentClass).getResultList();
            closeConnection();
            return all;
        } catch (Exception e) {
            throw new DAOException("ERRO AO BUSCAR TODOS OS OBJETOS", e);
        }
    }
}
