package dao.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import dao.Persistent;
import exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T, E> {

    private Class<T> persistentClass;
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    public GenericDAO(Class<T> persistentClass, EntityManagerFactory entityManagerFactory) {
        this.persistentClass = persistentClass;
        this.entityManagerFactory = entityManagerFactory;
    }

    private String getSelectSql() {
        return "SELECT obj FROM " + persistentClass.getSimpleName() + " obj";
    }

    protected void openConnection() {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    protected void closeConnection() {
        if (entityManager != null) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    @Override
    public Boolean create(T entity) throws DAOException {
        try {
            openConnection();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO ADICIONAR OBJETO", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public T read(E value) throws DAOException {
        try {
            openConnection();
            T entity = entityManager.find(persistentClass, value);
            entityManager.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            throw new DAOException("ERRO AO CONSULTAR OBJETO", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Boolean update(T entity) throws DAOException {
        try {
            openConnection();
            T toUpdate = entityManager.find(persistentClass, entity.getId());
            if (toUpdate == null) {
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
        } finally {
            closeConnection();
        }
    }

    @Override
    public Boolean delete(E value) throws DAOException {
        try {
            openConnection();
            T toDelete = entityManager.find(persistentClass, value);
            if (toDelete == null) {
                return false;
            } else {
                entityManager.remove(toDelete);
                entityManager.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DAOException("ERRO AO DELETAR OBJETO", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Collection<T> all() throws DAOException {
        try {
            openConnection();
            List<T> all = entityManager.createQuery(getSelectSql(), persistentClass).getResultList();
            return all;
        } catch (Exception e) {
            throw new DAOException("ERRO AO BUSCAR TODOS OS OBJETOS", e);
        } finally {
            closeConnection();
        }
    }
}
