package test.DAOTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

public class SaleDAOTest {

    private SaleDAO saleDAO = new SaleDAO("crud_Test");
    private IClientDAO clientDAO = new ClientDAO("crud_Test");
    private IProductDAO productDAO = new ProductDAO("crud_Test");

    private Client createClient(String cpf) throws DAOException {
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

    @AfterEach
    public void cleanup() throws DAOException {
        for (Sale sale : saleDAO.all()) {
            saleDAO.delete(sale.getId());
        }
        for (Client client : clientDAO.all()) {
            clientDAO.delete(client.getId());
        }
        for (Product product : productDAO.all()) {
            productDAO.delete(product.getId());
        }
    }

    @Test
    public void testCreate() throws DAOException {
        Client clientTest = createClient("10");
        Product productTest = createProduct("PS1", BigDecimal.ONE);
        Sale sale = new Sale();
        sale.setClient(clientTest);
        sale.setCode("S1");
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        sale.addProduct(productTest, 5);
        boolean created = saleDAO.create(sale);
        Assertions.assertTrue(created);
        Assertions.assertNotNull(sale.getId());
    }

    @Test
    public void testRead() throws DAOException {
        Client client = createClient("11");
        Product product = createProduct("PS2", BigDecimal.TWO);
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setCode("S2");
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        sale.addProduct(product, 2);
        saleDAO.create(sale);
        System.out.println(sale.getId() + "###aqui");
        Sale result = saleDAO.searchWithCollection(sale.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("S2", result.getCode());
        Assertions.assertEquals(new BigDecimal("4.00"), result.getTotalPrice());
        Sale resultByCode = saleDAO.searchByCode(sale.getCode());
        Assertions.assertNotNull(resultByCode);
        Assertions.assertEquals(new BigDecimal("4.00"), resultByCode.getTotalPrice());
    }

    @Test
    public void testUpdate() throws DAOException {
        Client client = createClient("12");
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
    public void testAll() throws DAOException {
        Client client = createClient("13");
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
        Collection<Sale> sales = saleDAO.all();
        Assertions.assertEquals(10, sales.size());
    }
}