package Test.DAOTest;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.IProductDAO;
import dao.ProductDAO;
import domain.Product;
import exceptions.DAOException;

public class ProductDAOTest {

    private IProductDAO productDAO;
    private Product productTest;

    public ProductDAOTest() {
        this.productDAO = new ProductDAO();
        this.productTest = new Product();
        productTest.setCode("P1");
        productTest.setName("teste");
        productTest.setDescription("descrição");
        productTest.setPrice(new BigDecimal(1.99));
        productTest.addCategory("CategoriaA");
        productTest.addCategory("CategoriaB");
    }

    @AfterEach
    public void cleanup() throws DAOException {
        for (Product product : productDAO.showAll()) {
            productDAO.delete(product.getId());
        }
    }

    @Test
    public void testCreate() throws DAOException {
        Boolean result = productDAO.create(productTest);
        Assertions.assertTrue(result);
        Assertions.assertNotNull(productTest.getId());
    }

    @Test
    public void testRead() throws DAOException {
        productDAO.create(productTest);
        Product result = productDAO.read(productTest.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(productTest.getCode(), result.getCode());
    }

    @Test
    public void testUpdate() throws DAOException {
        productDAO.create(productTest);
        Long id = productTest.getId();
        productTest.removeCategory("CategoriaB");
        productTest.addCategory("CategoriaC");
        Boolean updated = productDAO.update(productTest);
        Assertions.assertTrue(updated);
        Product result = productDAO.read(id);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getCategoryList().contains("CategoriaC"));
        Assertions.assertFalse(result.getCategoryList().contains("CategoriaB"));
    }

    @Test
    public void testDelete() throws DAOException {
        productDAO.create(productTest);
        Long id = productTest.getId();
        Boolean deleted = productDAO.delete(id);
        Assertions.assertTrue(deleted);
        Product result = productDAO.read(id);
        Assertions.assertNull(result);
    }

    @Test
    public void testShowAll() throws DAOException {
        productDAO.create(productTest);
        Product another = new Product();
        another.setCode("P2");
        another.setName("Produto 2");
        another.setDescription("Outro produto");
        another.setPrice(BigDecimal.TWO);
        another.addCategory("Utilidade");
        productDAO.create(another);
        Collection<Product> all = productDAO.showAll();
        Assertions.assertEquals(2, all.size());
    }
}
