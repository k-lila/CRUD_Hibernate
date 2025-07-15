package services.generic;

import java.io.Serializable;
import java.util.Collection;

import dao.Persistent;
import exceptions.DAOException;
import exceptions.ServiceException;

public interface IGenericService <T extends Persistent, E extends Serializable> {
    public Boolean register(T entity) throws DAOException, ServiceException;
    public T search(E value) throws DAOException, ServiceException;
    public Boolean edit(T entity) throws DAOException, ServiceException;
    public Boolean remove(E value) throws DAOException, ServiceException;
    public Collection<T> showAll() throws DAOException, ServiceException;
}
