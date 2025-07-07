package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dao.Persistent;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_PRODUCTS")
public class Product implements Persistent {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="products_seq")
	@SequenceGenerator(name="products_seq", sequenceName="sq_products", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "CODE", nullable = false, length = 11, unique = true)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 250)
    private String description;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @OneToOne(mappedBy = "product")
    private Stock stock;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "TB_PRODUCT_CATEGORY",
        joinColumns = @JoinColumn(name = "product_id")
    )
    private List<String> categoryList;

    public Product() {
        categoryList = new ArrayList<>();
    }

    public void addCategory(String category) {
        if (!categoryList.contains(category)) {
            categoryList.add(category);
        }
    }
    public void removeCategory(String category) {
        if (categoryList.contains(category)) {
            categoryList.remove(category);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        if (stock != null && stock.getProduct() != this) {
            stock.setProduct(this);
        }
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
