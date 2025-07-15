package services;

import domain.Product;
import domain.Stock;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.IGenericService;

public interface IStockService extends IGenericService<Stock, Long> {
    public Stock searchByProductCode(String code) throws DAOException, ServiceException;
    public Boolean checkQuantity(Product product, Integer quantity) throws DAOException, ServiceException;
}
