package dao.generic;

import java.io.Serializable;
import java.util.Collection;

import dao.Persistent;
import exceptions.DAOException;

public interface IGenericDAO <T extends Persistent, E extends Serializable> {
    public Boolean create(T entity) throws DAOException;
    public T read(E value) throws DAOException;
    public Boolean update(T entity) throws DAOException;
    public Boolean delete(E value) throws DAOException;
    public Collection<T> showAll() throws DAOException;
}
