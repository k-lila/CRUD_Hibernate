package Test.DAOTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dao.ClientDAO;
import dao.IClientDAO;
import dao.IProductDAO;
import dao.ProductDAO;
import dao.SaleDAO;
import domain.Client;
import domain.Product;
import domain.Sale;
import domain.Sale.Status;
import exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaleDAOTest {

    private EntityManagerFactory entityManagerFactoryTest;
    private EntityManager entityManagerTest;

    private SaleDAO saleDAO = new SaleDAO();
    private IClientDAO clientDAO = new ClientDAO();
    private IProductDAO productDAO = new ProductDAO();

    private Client createClient(Long cpf) throws DAOException {
        Client client = new Client();
        client.setName("Cliente Teste");
        client.setCpf(cpf);
        client.setCity("Cidade");
        client.setAddress("Rua Teste");
        client.setNumber(10);
        client.setFone(999999999L);
        client.setState("Estado");
        client.setCreationDate(Instant.now());
        clientDAO.create(client);
        return client;
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
        entityManagerTest.createNativeQuery("TRUNCATE TABLE tb_product_quantity CASCADE").executeUpdate();
        entityManagerTest.createNativeQuery("TRUNCATE TABLE tb_sales CASCADE").executeUpdate();
        entityManagerTest.createNativeQuery("TRUNCATE TABLE tb_products CASCADE").executeUpdate();
        entityManagerTest.createNativeQuery("TRUNCATE TABLE tb_clients CASCADE").executeUpdate();
        entityManagerTest.getTransaction().commit();
        if (entityManagerTest.isOpen()) entityManagerTest.close();
    }

    @AfterAll
    public void close() {
        if (entityManagerFactoryTest.isOpen()) entityManagerFactoryTest.close();
    }

    @Test
    public void testCreateSale() throws DAOException {
        Client clientTest = createClient(10l);
        Product productTest = createProduct("PS1", BigDecimal.ONE);
        Sale sale = new Sale();
        sale.setClient(clientTest);
        sale.setCode("S1");
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        sale.addProduct(productTest, 1);
        boolean created = saleDAO.create(sale);
        Assertions.assertTrue(created);
        Assertions.assertNotNull(sale.getId());
    }

    @Test
    public void testReadSale() throws DAOException {
        Client client = createClient(11l);
        Product product = createProduct("PS2", BigDecimal.TWO);
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setCode("S2");
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        sale.addProduct(product, 2);
        saleDAO.create(sale);
        Sale result = saleDAO.searchWithCollection(sale.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("S2", result.getCode());
        Assertions.assertEquals(new BigDecimal("4.00"), result.getTotalPrice());
    }

    @Test
    public void testUpdateSale() throws DAOException {
        Client client = createClient(12l);
        Product product = createProduct("PS3", BigDecimal.ONE);
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setCode("S3");
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        sale.addProduct(product, 10);
        boolean mustTrue = saleDAO.create(sale);
        Assertions.assertTrue(mustTrue);
        sale = saleDAO.searchWithCollection(sale.getId());
        sale.removeProduct(product, 3);
        sale.setStatus(Status.CONCLUIDA);
        boolean updated = saleDAO.update(sale);
        Assertions.assertTrue(updated);
        Sale result = saleDAO.searchWithCollection(sale.getId());
        Assertions.assertEquals(7, result.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("7.00"), result.getTotalPrice());
        Assertions.assertEquals(Status.CONCLUIDA, result.getStatus());
    }

    @Test
    public void testShowAllSales() throws DAOException {
        Client client = createClient(13l);
        Product product = createProduct("PS4", BigDecimal.ONE);
        for (int i = 0; i < 10; i++) {
            Sale sale = new Sale();
            sale.setClient(client);
            sale.setCode("S4" + i);
            sale.setDate(Instant.now());
            sale.addProduct(product, 1);
            sale.setStatus(Status.CONCLUIDA);
            saleDAO.create(sale);
        }
        Collection<Sale> sales = saleDAO.showAll();
        Assertions.assertEquals(10, sales.size());
    }
}