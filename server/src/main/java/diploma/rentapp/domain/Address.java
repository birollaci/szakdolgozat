package diploma.rentapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String country;

    private String county;

    @NotBlank
    private String city;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String streetName;
    
    @Positive
    private int number;

    private String door;

    public Address() {
    }

    public Address(String country, String county, String city, String zipCode, String streetName, int number, String door) {
        this.country = country;
        this.county = county;
        this.city = city;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.number = number;
        this.door = door;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getnumber() {
        return number;
    }

    public void setnumber(int number) {
        this.number = number;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public Address copyValidValuesFrom(Address address) {
        if (address.country != null)
            this.country = address.country;
        if (address.county != null)
            this.county = address.county;
        if (address.city != null)
            this.city = address.city;
        if (address.zipCode != null)
            this.zipCode = address.zipCode;
        if (address.streetName != null)
            this.streetName = address.streetName;
        if (address.number > 0)
            this.number = address.number;
        if (address.door != null)
            this.door = address.door;
        return this;
    }

    @Override
    public String toString() {
        return "Address [city=" + city + 
        ", country=" + country + 
        ", county=" + county + 
        ", door=" + door + 
        ", id=" + id + 
        ", streetName=" + streetName + 
        ", number=" + number + 
        ", zipCode=" + zipCode + "]";
    }
}

// test:
// { 
//     "id": 1,
//     "country": "MO",
//     "city": "Bp",
//     "zipCode": "1117",
//     "streetName": "almafa utca"
//  }