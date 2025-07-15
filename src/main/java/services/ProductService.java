package services;

import dao.IProductDAO;
import domain.Product;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.GenericService;

public class ProductService extends GenericService<Product, Long> implements IProductService{
    private final IProductDAO productDAO;

    public ProductService(IProductDAO iProductDAO) {
        super(iProductDAO);
        this.productDAO = iProductDAO;
    }

    @Override
    public Product searchByCode(String code) throws DAOException, ServiceException {
        try {
            return productDAO.searchByCode(code);
        } catch(Exception e) {
            throw new ServiceException("Erro ao procurar produto pelo código", e);
        }
    }
}
