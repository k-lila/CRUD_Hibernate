package dao;

import dao.generic.IGenericDAO;
import domain.Stock;
import exceptions.DAOException;

public interface IStockDAO extends IGenericDAO<Stock, Long> {
    public Stock searchByProductCode(String code) throws DAOException;
    public Boolean verifyQuantity(String code, Integer quantity) throws DAOException;
}
