package test.serviceTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.ClientDAO;
import dao.IClientDAO;
import dao.IProductDAO;
import dao.ISaleDAO;
import dao.IStockDAO;
import dao.ProductDAO;
import dao.SaleDAO;
import dao.StockDAO;
import dao.singleton.EntityManagerSingleton;
import domain.Client;
import domain.Product;
import domain.Sale;
import domain.Stock;
import domain.Sale.Status;
import exceptions.DAOException;
import exceptions.ServiceException;
import jakarta.persistence.EntityManagerFactory;
import services.ISaleService;
import services.SaleService;

public class SaleServiceTest {

    private ISaleDAO saleDAOTest;
    private IClientDAO clientDAOtest;
    private IProductDAO productDAOtest;
    private IStockDAO stockDAOtest;

    private ISaleService saleService;

    public SaleServiceTest() {
        EntityManagerFactory singleton = EntityManagerSingleton.getInstance();
        saleDAOTest = new SaleDAO(singleton);
        clientDAOtest = new ClientDAO(singleton);
        productDAOtest = new ProductDAO(singleton);
        stockDAOtest = new StockDAO(singleton);

        ISaleDAO _saleDAO = new SaleDAO(singleton);
        IStockDAO _stockDAO = new StockDAO(singleton);
        saleService = new SaleService(_saleDAO, _stockDAO);
    }

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
        clientDAOtest.create(client);
        return client;
    }

    private Product createProduct(String code) throws DAOException {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto " + code);
        product.setDescription("Descrição " + code);
        product.setPrice(BigDecimal.ONE);
        productDAOtest.create(product);
        return product;
    }

    private Stock createStock(Product product) throws DAOException {
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stockDAOtest.create(stock);
        return stock;
    }

    private Sale createSale(String code, Client client, Product product) {
        Sale sale = new Sale();
        sale.setCode(code);
        sale.setClient(client);
        sale.addProduct(product, 2);
        sale.setDate(Instant.now());
        sale.setStatus(Status.INICIADA);
        return sale;
    }

    @AfterEach
    public void cleanup() throws DAOException {
        for (Sale sale : saleDAOTest.all()) {
            saleDAOTest.delete(sale.getId());
        }
        for (Stock stock : stockDAOtest.all()) {
            stockDAOtest.delete(stock.getId());
        }
        for (Client client : clientDAOtest.all()) {
            clientDAOtest.delete(client.getId());
        }
        for (Product product : productDAOtest.all()) {
            productDAOtest.delete(product.getId());
        }
    }

    @Test
    public void createTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        Boolean create = saleService.register(sale);
        Assertions.assertTrue(create);
    }

    @Test
    public void readTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        saleService.register(sale);
        Sale readA = saleService.search(sale.getId());
        Sale readB = saleService.searchByCode("service");
        Assertions.assertNotNull(readA);
        Assertions.assertNotNull(readB);
    }

    @Test
    public void updateTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        saleService.register(sale);
        Sale update = saleService.searchWithCollection(sale.getId());
        update.addProduct(product, 3);
        Boolean updated = saleService.edit(update);
        Assertions.assertTrue(updated);
    }

    @Test
    public void deleteTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        saleService.register(sale);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            saleService.remove(sale.getId());
        });
    }

    @Test
    public void allTest() throws DAOException, ServiceException {
        for (int i = 0; i < 5; i++) {
            Client client = createClient(String.valueOf(i));
            Product product = createProduct(String.valueOf(i));
            createStock(product);
            Sale sale = createSale(String.valueOf(i), client, product);
            saleService.register(sale);
        }
        Collection<Sale> all = saleService.showAll();
        Assertions.assertEquals(5, all.size());
    }

    @Test
    public void closeSaleTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        saleService.register(sale);
        Boolean close = saleService.closeSale(sale);
        Assertions.assertTrue(close);
        Stock stock = stockDAOtest.searchByProductCode("service");
        Assertions.assertEquals(8, stock.getQuantity());
    }

    @Test
    public void closeSaleTestB() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        sale.addProduct(product, 10);
        saleService.register(sale);
        Assertions.assertThrows(ServiceException.class, () -> {
            saleService.closeSale(sale);
        });
    }

    @Test
    public void cancelSale() throws DAOException, ServiceException {
        Client client = createClient("service");
        Product product = createProduct("service");
        createStock(product);
        Sale sale = createSale("service", client, product);
        sale.addProduct(product, 3);
        saleService.register(sale);
        Boolean close = saleService.closeSale(sale);
        Assertions.assertTrue(close);
        Stock stock = stockDAOtest.searchByProductCode("service");
        Assertions.assertEquals(5, stock.getQuantity());
        Boolean canceled = saleService.cancelSale(sale);
        Assertions.assertTrue(canceled);
        stock = stockDAOtest.searchByProductCode("service");
        Assertions.assertEquals(10, stock.getQuantity());
    }
}
