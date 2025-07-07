
package domain;

import dao.Persistent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_STOCK")
public class Stock implements Persistent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    @SequenceGenerator(name = "stock_seq", sequenceName = "seq_stock", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(
        name = "id_product_fk",
        referencedColumnName = "id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_stock_product")
    )
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

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
        if (product != null && product.getStock() != this) {
            product.setStock(this);
        }
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
