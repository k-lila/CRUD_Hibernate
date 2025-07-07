package domain;

import java.time.Instant;

import dao.Persistent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_CLIENTS")
public class Client implements Persistent {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="clients_seq")
	@SequenceGenerator(name="clients_seq", sequenceName="sq_client", initialValue = 1, allocationSize = 1)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "CPF", nullable = false, unique = true)
    private Long cpf;

    @Column(name = "FONE")
    private Long fone;

    @Column(name = "ADDRESS", length = 100)
    private String address;

    @Column(name = "NUMBER")
    private Integer number;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "STATE", length = 50)
    private String state;

    @Column(name = "CREATION_DATE", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant creationDate;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getCpf() {
        return cpf;
    }
    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }
    public Long getFone() {
        return fone;
    }
    public void setFone(Long fone) {
        this.fone = fone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public Instant getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}
