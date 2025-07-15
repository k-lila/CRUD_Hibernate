package services.generic;

import java.io.Serializable;
import java.util.Collection;

import dao.Persistent;
import dao.generic.IGenericDAO;
import exceptions.DAOException;
import exceptions.ServiceException;

public abstract class GenericService<T extends Persistent, E extends Serializable> implements IGenericService <T, E> {
    protected IGenericDAO<T, E> dao;

    public GenericService(IGenericDAO<T, E> dao) {
        this.dao = dao;
    }

    @Override
    public Boolean register(T entity) throws ServiceException, DAOException {
        try {
            return this.dao.create(entity);
        } catch(Exception e) {
            throw new ServiceException("Erro ao registrar objeto", e);
        }
    }

    @Override
    public T search(E value) throws DAOException, ServiceException {
        try {
            return this.dao.read(value);
        } catch(Exception e) {
            throw new ServiceException("Erro ao procurar objeto", e);
        }
    }

    @Override
    public Boolean edit(T entity) throws DAOException, ServiceException {
        try {
            return this.dao.update(entity);
        } catch(Exception e) {
            throw new ServiceException("Erro ao editar objeto", e);
        }
    }

    @Override
    public Boolean remove(E value) throws DAOException, ServiceException {
        try {
            return this.dao.delete(value);
        } catch(Exception e) {
            throw new ServiceException("Erro ao remover objeto", e);
        }
    }

    @Override
    public Collection<T> showAll() throws DAOException, ServiceException {
        try {
            return this.dao.all();
        } catch(Exception e) {
            throw new ServiceException("Erro ao mostrar todos objetos", e);
        }
    }
}
