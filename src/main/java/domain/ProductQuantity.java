package domain;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_PRODUCT_QUANTITY")
public class ProductQuantity {

    @Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="p_qty_seq")
	@SequenceGenerator(name="p_qty_seq", sequenceName="sq_prod_qty", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_sale_fk", 
		foreignKey = @ForeignKey(name = "fk_prod_qty_sale"), 
		referencedColumnName = "id", nullable = false
	)
    private Sale sale;

    public ProductQuantity() {
        quantity = 0;
        totalPrice = BigDecimal.ZERO;
    }

    public void addQuantity(Integer num) {
        quantity += num;
        BigDecimal newValue = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        totalPrice = newValue;
    }

    public void removeQuantity(Integer num) {
        quantity = Math.max(quantity - num, 0);
        BigDecimal newPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        totalPrice = newPrice;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Sale getSale() {
        return sale;
    }
    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
