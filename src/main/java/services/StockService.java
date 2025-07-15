package services;

import dao.IStockDAO;
import domain.Product;
import domain.Stock;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.GenericService;

public class StockService extends GenericService<Stock, Long> implements IStockService {

    private final IStockDAO iStockDAO;

    public StockService(IStockDAO dao) {
        super(dao);
        this.iStockDAO = dao;
    }

    @Override
    public Stock searchByProductCode(String code) throws DAOException, ServiceException {
        try {
            return iStockDAO.searchByProductCode(code);
        } catch(Exception e) {
            throw new ServiceException("Erro ao buscar pelo código do produto", e);
        }
    }

    @Override
    public Boolean checkQuantity(Product product, Integer quantity) throws DAOException, ServiceException {
        try {
            return iStockDAO.verifyQuantity(product.getCode(), quantity);
        } catch(Exception e) {
            throw new ServiceException("Erro ao checar quantitidade", e);
        }
    }
}
