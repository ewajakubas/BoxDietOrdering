package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CLIENT")
@DiscriminatorValue("CLIENT")

@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id"),
    @NamedQuery(name = "Client.findByCity", query = "SELECT c FROM Client c WHERE c.city = :city"),
    @NamedQuery(name = "Client.findByZipCode", query = "SELECT c FROM Client c WHERE c.zipCode = :zipCode"),
    @NamedQuery(name = "Client.findByStreet", query = "SELECT c FROM Client c WHERE c.street = :street"),
    @NamedQuery(name = "Client.findByHouseNumber", query = "SELECT c FROM Client c WHERE c.houseNumber = :houseNumber"),
    @NamedQuery(name = "Client.findByApartmentNumber", query = "SELECT c FROM Client c WHERE c.apartmentNumber = :apartmentNumber"),
    })

public class Client extends Account implements Serializable {
   
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CITY", nullable=false)
    private String city;
    
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ZIP_CODE", nullable=false)
    private String zipCode;
    
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "STREET", nullable=false)
    private String street;
    
    @NotNull
    @Column(name = "HOUSE_NUMBER", nullable=false)
    private int houseNumber;
    
    @Column(name = "APARTMENT_NUMBER", nullable=true)
    private int apartmentNumber;
    
    @OneToMany(mappedBy = "whoOrdered")
    private List<ClientOrder> clientOrderList = new ArrayList<ClientOrder>();
   
    public List<ClientOrder> getClientOrderList() {
        return clientOrderList;
    }

    public Client() {
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

}

