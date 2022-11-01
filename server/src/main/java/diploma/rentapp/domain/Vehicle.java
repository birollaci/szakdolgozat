package diploma.rentapp.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
public class Vehicle {

    public Vehicle() {}

    public Vehicle(String name, String brand, String year, int price, String description, ECategory category, Location location) {
        this.name = name;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.description = description;
        this.category = category;
        this.location = location;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String brand;

    private String year;

    @Positive
    private int price;

    @Size(min = 5, max = 80)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECategory category;

    @NotNull
    @ManyToOne
    private Location location;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ECategory getCategory() {
        return category;
    }

    public void setCategory(ECategory category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vehicle copyValidValuesFrom(Vehicle vehicle) {
        if (vehicle.id != null)
            this.id = vehicle.id;
        if (vehicle.name != null)
            this.name = vehicle.name;
        if (vehicle.brand != null)
            this.brand = vehicle.brand;
        if (vehicle.year != null)
            this.year = vehicle.year;
        if (vehicle.price != 0)
            this.price = vehicle.price;
        if (vehicle.description != null)
            this.description = vehicle.description;
        if (vehicle.category != null)
            this.category = vehicle.category;
        if (vehicle.location != null)
            this.location.copyValidValuesFrom(vehicle.location);
        
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", brand='" + getBrand() + "'" +
                ", year='" + getYear() + "'" +
                ", price='" + getPrice() + "'" +
                ", description='" + getDescription() + "'" +
                ", category='" + getCategory() + "'" +
                ", location='" + getLocation() + "'" +
                "}";
    }
}

// {
//     "id": 1,
//     "name": "Termek1",
//     "price": 15,
//     "description": "Ez egy leiras1",
//     "category": "OTHER"
// }