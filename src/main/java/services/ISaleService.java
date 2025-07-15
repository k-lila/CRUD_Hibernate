package services;

import domain.Sale;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.IGenericService;

public interface ISaleService extends IGenericService<Sale, Long>  {
    public Boolean closeSale(Sale sale) throws DAOException, ServiceException;
    public Boolean cancelSale(Sale sale) throws DAOException, ServiceException;
    public Sale searchByCode(String code) throws DAOException, ServiceException;
    public Sale searchWithCollection(Long id) throws DAOException, ServiceException;
}
