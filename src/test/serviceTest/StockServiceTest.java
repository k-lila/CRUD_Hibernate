package test.serviceTest;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.IProductDAO;
import dao.IStockDAO;
import dao.ProductDAO;
import dao.StockDAO;
import domain.Product;
import domain.Stock;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.IStockService;
import services.StockService;

public class StockServiceTest {

    private IStockService stockService;
    private IProductDAO iProductDAO;

    public StockServiceTest() {
    
        IStockDAO dao = new StockDAO();
        stockService = new StockService(dao);
        iProductDAO = new ProductDAO();
    }

    private Product createProduct(String code) throws DAOException {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto " + code);
        product.setDescription("Descrição " + code);
        product.setPrice(BigDecimal.ONE);
        return product;
    }

    @AfterEach
    public void cleanup() throws DAOException, ServiceException {
        for (Stock stock : stockService.showAll()) {
            stockService.remove(stock.getId());
        }
        for (Product product : iProductDAO.all()) {
            iProductDAO.delete(product.getId());
        }
    }

    @Test
    public void createTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        iProductDAO.create(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        Boolean add = stockService.register(stock);
        Assertions.assertTrue(add);
    }

    @Test
    public void readTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        iProductDAO.create(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stockService.register(stock);
        Stock readA = stockService.search(stock.getId());
        Stock readB = stockService.searchByProductCode(product.getCode());
        Assertions.assertNotNull(readA);
        Assertions.assertNotNull(readB);
    }

    @Test
    public void updateTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        iProductDAO.create(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stockService.register(stock);
        Stock update = stockService.search(stock.getId());
        update.setQuantity(0);
        Boolean updated = stockService.edit(update);
        Assertions.assertTrue(updated);
    }

    @Test
    public void deleteTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        iProductDAO.create(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stockService.register(stock);
        Boolean delete = stockService.remove(stock.getId());
        Assertions.assertTrue(delete);
    }

    @Test
    public void verifyQuantityTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        iProductDAO.create(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stockService.register(stock);
        Boolean check = stockService.checkQuantity(product, 10);
        Assertions.assertTrue(check);
    }

    @Test
    public void allTest() throws DAOException, ServiceException {
        for (int i = 0; i < 5; i++) {
            Product p = createProduct(String.valueOf(i));
            iProductDAO.create(p);
            Stock s = new Stock();
            s.setProduct(p);
            s.setQuantity(1);
            stockService.register(s);
        }
        Collection<Stock> all = stockService.showAll();
        Assertions.assertEquals(5, all.size());
    }
}
