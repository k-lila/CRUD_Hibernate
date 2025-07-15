package services;

import dao.ISaleDAO;
import dao.IStockDAO;
import domain.ProductQuantity;
import domain.Sale;
import domain.Stock;
import domain.Sale.Status;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.GenericService;

public class SaleService extends GenericService<Sale, Long> implements ISaleService {

    private final ISaleDAO saleDAO;
    private final IStockDAO stockDAO;

    public SaleService(ISaleDAO iSaleDAO, IStockDAO iStockDAO) {
        super(iSaleDAO);
        this.saleDAO = iSaleDAO;
        this.stockDAO = iStockDAO;
    }

    @Override
    public Boolean remove(Long id) throws DAOException, ServiceException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Boolean closeSale(Sale sale) throws DAOException, ServiceException {
        try {
            for (ProductQuantity productQuantity : sale.getProductList()) {
                Stock stock = stockDAO.searchByProductCode(productQuantity.getProduct().getCode());
                Boolean checkStock = stockDAO.verifyQuantity(productQuantity.getProduct().getCode(), productQuantity.getQuantity());
                if (checkStock) {
                    stock.setQuantity(stock.getQuantity() - productQuantity.getQuantity());
                    stockDAO.update(stock);
                } else {
                    throw new ServiceException("ESTOQUE INSUFICIENTE", null);
                }
            }
            return ((ISaleDAO) this.dao).completeSale(sale);
        } catch(Exception e) {
            throw new ServiceException("Erro ao completar venda", e);
        }
    }

    @Override
    public Boolean cancelSale(Sale sale) throws DAOException, ServiceException {
        try {
            if (sale.getStatus() == Status.CONCLUIDA) {
                for (ProductQuantity productQuantity : sale.getProductList()) {
                    Stock stock = stockDAO.searchByProductCode(productQuantity.getProduct().getCode());
                    stock.setQuantity(stock.getQuantity() + productQuantity.getQuantity());
                    stockDAO.update(stock);
                }
            }
            return ((ISaleDAO) this.dao).cancelSale(sale);
        } catch (Exception e) {
            throw new ServiceException("Erro ao cancelar venda", e);
        }
    }

    @Override
    public Sale searchByCode(String code) throws DAOException, ServiceException {
        try {
            return saleDAO.searchByCode(code);
        } catch(Exception e) {
            throw new ServiceException("Erro ao procurar venda pelo código", e);
        }
    }

    @Override
    public Sale searchWithCollection(Long id) throws DAOException, ServiceException {
        try {
            return saleDAO.searchWithCollection(id);
        } catch(Exception e) {
            throw new ServiceException("Erro ao procurar venda com produtos", e);
        }
    }
}
