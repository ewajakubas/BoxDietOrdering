package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ClientDTO extends AccountDTO{
  
    @NotNull(message="{constraint.notnull}")
    @Size(min=2,max=32,message="{constraint.string.length.notinrange}")
    private String city;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min=5,max=32,message="{constraint.string.length.notinrange}")
   @Pattern(regexp="^\\d{2}-\\d{3}$",message="{constraint.string.incorrectchar}")
    private String zipCode;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min=2,max=32,message="{constraint.string.length.notinrange}")
    private String street;
     
    @NotNull(message="{constraint.notnull}")
    @Min(value=1)
    private int houseNumber;
    
    private int apartmentNumber;
    
    public ClientDTO(){
    }

    public ClientDTO(String city, String zipCode, String street, int houseNumber, int apartmentNumber, Long id, String login, boolean active, String type, String name, String surname, String email, String phone, String question, String answer) {
        super(id, login, active, type, name, surname, email, phone, question, answer);
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
//
//    public ClientDTO(Long id, String login, Boolean active, String type, String name, String surname, String email, String phone) {
//       
//    }

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

    @Override
    public String toString() {
        return "ClientDTO{" + "city=" + city + ", zipCode=" + zipCode + ", street=" + street + ", houseNumber=" + houseNumber + ", apartmentNumber=" + apartmentNumber + '}';
    }
    
    
}
