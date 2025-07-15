package services;

import domain.Product;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.IGenericService;

public interface IProductService extends IGenericService<Product, Long> {
    public Product searchByCode(String code) throws DAOException, ServiceException;
}
