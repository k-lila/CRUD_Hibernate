package test.serviceTest;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.IProductDAO;
import dao.ProductDAO;
import dao.singleton.EntityManagerSingleton;
import domain.Product;
import exceptions.DAOException;
import exceptions.ServiceException;
import jakarta.persistence.EntityManagerFactory;
import services.IProductService;
import services.ProductService;

public class ProductServiceTest {

    private IProductService productService;

    public ProductServiceTest() {
        EntityManagerFactory singleton = EntityManagerSingleton.getInstance();
        IProductDAO dao = new ProductDAO(singleton);
        productService = new ProductService(dao);
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
        for (Product product : productService.showAll()) {
            productService.remove(product.getId());
        }
    }

    @Test
    public void createTest() throws DAOException, ServiceException {
        Boolean add = productService.register(createProduct("service"));
        Assertions.assertTrue(add);
    }

    @Test
    public void readTest() throws Exception, ServiceException {
        Product product = createProduct("service");
        productService.register(product);
        Product read = productService.search(product.getId());
        Assertions.assertNotNull(read);
        Product readByCode = productService.searchByCode("service");
        Assertions.assertNotNull(readByCode);
    }

    @Test void updateTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        productService.register(product);
        Product read = productService.search(product.getId());
        read.setName("Modificado");
        Boolean edited = productService.edit(read);
        Assertions.assertTrue(edited);
    }

    @Test
    public void deleteTest() throws DAOException, ServiceException {
        Product product = createProduct("service");
        Boolean add = productService.register(product);
        Assertions.assertTrue(add);
        Boolean delete = productService.remove(product.getId());
        Assertions.assertTrue(delete);
    }

    @Test
    public void showAllTest() throws DAOException, ServiceException {
        for (int i = 0; i < 5; i++) {
            productService.register(createProduct(String.valueOf(i)));
        }
        Collection<Product> all = productService.showAll();
        Assertions.assertEquals(5, all.size());
    }
}
