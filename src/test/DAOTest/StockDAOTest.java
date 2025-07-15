package test.DAOTest;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.ProductDAO;
import dao.StockDAO;
import domain.Product;
import domain.Stock;
import exceptions.DAOException;

public class StockDAOTest {

    private ProductDAO productDAO;
    private StockDAO stockDAO;

    public StockDAOTest() {
        productDAO = new ProductDAO();
        stockDAO = new StockDAO();
    }

    private Product createProduct(String code, BigDecimal price) throws DAOException {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto " + code);
        product.setDescription("Descrição " + code);
        product.setPrice(price);
        productDAO.create(product);
        return product;
    }

    @AfterEach
    public void cleanup() throws DAOException {
        for (Stock stock : stockDAO.all()) {
            stockDAO.delete(stock.getId());
        }
        for (Product product : productDAO.all()) {
            productDAO.delete(product.getId());
        }
    }

    @Test
    public void testCreate() throws DAOException {
        Product product = createProduct("ST1", BigDecimal.ONE);
        Assertions.assertNotNull(product);
        Stock stockTest = new Stock();
        stockTest.setProduct(product);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Assertions.assertNotNull(stockTest.getId());
    }

    @Test
    public void testRead() throws DAOException {
        Product product = createProduct("ST2", BigDecimal.ONE);
        Assertions.assertNotNull(product);
        Stock stockTest = new Stock();
        stockTest.setProduct(product);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Stock searched = stockDAO.read(stockTest.getId());
        Assertions.assertNotNull(searched);
        Assertions.assertEquals(product.getCode(), searched.getProduct().getCode());
        Assertions.assertEquals(10, searched.getQuantity());
    }

    @Test
    public void testReadWithCode() throws DAOException {
        Product product = createProduct("ST3", BigDecimal.ONE);
        Assertions.assertNotNull(product);
        Stock stockTest = new Stock();
        stockTest.setProduct(product);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Stock searched = stockDAO.searchByProductCode(stockTest.getProduct().getCode());
        Assertions.assertNotNull(searched);
        Assertions.assertEquals(product.getCode(), searched.getProduct().getCode());
        Assertions.assertEquals(10, searched.getQuantity());
    }


    @Test
    public void testUpdate() throws DAOException {
        Product productA = createProduct("ST4A", BigDecimal.ONE);
        Product productB = createProduct("ST4B", BigDecimal.ONE);
        Assertions.assertNotNull(productA);
        Assertions.assertNotNull(productB);
        Stock stockTest = new Stock();
        stockTest.setProduct(productA);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Stock searched = stockDAO.read(stockTest.getId());
        searched.setProduct(productB);
        searched.setQuantity(5);
        Boolean updated = stockDAO.update(searched);
        Assertions.assertTrue(updated);
        Assertions.assertEquals(productB.getCode(), searched.getProduct().getCode());
        Assertions.assertEquals(5, searched.getQuantity());
    }

    @Test
    public void testDelete() throws DAOException {
        Product product = createProduct("ST5", BigDecimal.ONE);
        Assertions.assertNotNull(product);
        Stock stockTest = new Stock();
        stockTest.setProduct(product);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Boolean deleted = stockDAO.delete(stockTest.getId());
        Assertions.assertTrue(deleted);
    }

    @Test
    public void testCheckQuantity() throws DAOException {
        Product product = createProduct("ST6", BigDecimal.ONE);
        Assertions.assertNotNull(product);
        Stock stockTest = new Stock();
        stockTest.setProduct(product);
        stockTest.setQuantity(10);
        Boolean addStock = stockDAO.create(stockTest);
        Assertions.assertTrue(addStock);
        Boolean verifyQuantity = stockDAO.verifyQuantity("ST6",10);
        Assertions.assertTrue(verifyQuantity);
        verifyQuantity = stockDAO.verifyQuantity("ST6",20);
        Assertions.assertFalse(verifyQuantity);
    }

    @Test
    public void testAll() throws DAOException {
        for (int i = 0; i < 5; i++) {
            Product p = createProduct(String.valueOf(i), BigDecimal.ONE);
            Stock s = new Stock();
            s.setProduct(p);
            s.setQuantity(1);
            stockDAO.create(s);
        }
        Collection<Stock> all = stockDAO.all();
        Assertions.assertEquals(5, all.size());
    }
}
