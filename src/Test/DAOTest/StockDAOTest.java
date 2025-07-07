package Test.DAOTest;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dao.ProductDAO;
import dao.StockDAO;
import domain.Product;
import domain.Stock;
import exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockDAOTest {

    private EntityManagerFactory entityManagerFactoryTest;
    private EntityManager entityManagerTest;

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

    @BeforeAll
    public void init() {
        entityManagerFactoryTest = Persistence.createEntityManagerFactory("crud_JPA");
    }

    @BeforeEach
    public void setup() {
        entityManagerTest = entityManagerFactoryTest.createEntityManager();
    }

    @AfterEach
    public void cleanup() throws DAOException {
        entityManagerTest.getTransaction().begin();
        entityManagerTest.createNativeQuery("TRUNCATE TABLE tb_products CASCADE").executeUpdate();
        entityManagerTest.getTransaction().commit();
        if (entityManagerTest.isOpen()) entityManagerTest.close();
    }

    @AfterAll
    public void close() {
        if (entityManagerFactoryTest.isOpen()) entityManagerFactoryTest.close();
    }

    @Test
    public void createStockEntryTest() throws DAOException {
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
    public void readStockEntryTest() throws DAOException {
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
    public void readWithCodeStockEntryTest() throws DAOException {
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
    public void updateStockEntryTest() throws DAOException {
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
    public void deleteStockEntryTest() throws DAOException {
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
    public void checkQuantity() throws DAOException {
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
}
