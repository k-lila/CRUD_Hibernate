package dao;

import dao.generic.IGenericDAO;
import domain.Sale;
import exceptions.DAOException;

public interface ISaleDAO extends IGenericDAO<Sale, Long> {
    public Boolean completeSale(Sale sale) throws DAOException;
    public Boolean cancelSale(Sale sale) throws DAOException;
    public Sale searchWithCollection(Long saleId) throws DAOException;
}
