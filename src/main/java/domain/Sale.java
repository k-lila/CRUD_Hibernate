package domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.Persistent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_SALES")
public class Sale implements Persistent {
    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;
        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sale_seq")
	@SequenceGenerator(name="sale_seq", sequenceName="sq_sale", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

	@ManyToOne
	@JoinColumn(name = "id_client_fk", 
		foreignKey = @ForeignKey(name = "fk_sale_client"), 
		referencedColumnName = "id", nullable = false
	)
    private Client client;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "DATE", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<ProductQuantity> productList;

    public Sale() {
        this.productList = new ArrayList<>();
    }

    private void validateStatus() {
		if (this.status == Status.CONCLUIDA || this.status == Status.CANCELADA) {
			throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA OU CANCELADA");
		}
	}
    public void addProduct(Product product, Integer quantity) {
        validateStatus();
        Optional<ProductQuantity> op = productList.stream().filter(f -> f.getProduct().getCode().equals(product.getCode())).findAny();
        if (op.isPresent()) {
            ProductQuantity productQuantity = op.get();
            productQuantity.addQuantity(quantity);
        } else {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.setSale(this);
            productQuantity.addQuantity(quantity);
            productList.add(productQuantity);
        }
        recalculateTotal();
    }
    public void removeProduct(Product product, Integer quantity) {
        validateStatus();
        Optional<ProductQuantity> op = productList.stream().filter(f -> f.getProduct().getCode().equals(product.getCode())).findAny();    
        if (op.isPresent()) {
            ProductQuantity productQuantity = op.get();
            if (productQuantity.getQuantity() > quantity) {
                productQuantity.removeQuantity(quantity);
                recalculateTotal();
            } else {
                productList.remove(op.get());
                recalculateTotal();
            }
            recalculateTotal();
        }
    }
    public void removeAllProducts() {
		validateStatus();
		productList.clear();
		totalPrice = BigDecimal.ZERO;
	}
    public Integer getTotalQuantity() {
        Integer quantity = productList.stream().reduce(0, (a, b) -> a + b.getQuantity(), Integer::sum);
        return quantity;
    }
    public void recalculateTotal() {
        validateStatus();
        BigDecimal total = BigDecimal.ZERO;
        for (ProductQuantity prod : productList) {
            total = total.add(prod.getTotalPrice());
        }
        this.totalPrice = total;
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
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Instant getDate() {
        return date;
    }
    public void setDate(Instant date) {
        this.date = date;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public List<ProductQuantity> getProductList() {
        return productList;
    }
    public void setProductList(List<ProductQuantity> productList) {
        this.productList = productList;
    }
}
