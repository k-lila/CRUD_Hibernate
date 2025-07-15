package dao;

import dao.generic.IGenericDAO;
import domain.Product;
import exceptions.DAOException;

public interface IProductDAO extends IGenericDAO<Product, Long>{
    public Product searchByCode(String code) throws DAOException;
}
