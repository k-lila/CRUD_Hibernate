package dao;

import dao.generic.GenericDAO;
import domain.Product;

public class ProductDAO extends GenericDAO<Product, Long> implements IProductDAO {
    public ProductDAO() {
        super(Product.class);
    }
}
