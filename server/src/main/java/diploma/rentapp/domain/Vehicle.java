package diploma.rentapp.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
public class Vehicle {

    public Vehicle() {}

    public Vehicle(String name, String brand, int price, String description, ECategory category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.category = category;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String brand;

    @Positive
    private int price;

    @Size(max = 200)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECategory category;

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

    public Vehicle copyValidValuesFrom(Vehicle vehicle) {
        if (vehicle.id != null)
            this.id = vehicle.id;
        if (vehicle.name != null)
            this.name = vehicle.name;
        if (vehicle.brand != null)
            this.brand = vehicle.brand;
        if (vehicle.price != 0)
            this.price = vehicle.price;
        if (vehicle.description != null)
            this.description = vehicle.description;
        if (vehicle.category != null)
            this.category = vehicle.category;
        
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", brand='" + getBrand() + "'" +
                ", price='" + getPrice() + "'" +
                ", description='" + getDescription() + "'" +
                ", category='" + getCategory() + "'" +
                "}";
    }
}

// {
//     "id": 1,
//     "name": "Jarmu1",
//     "brand": "Marka1"
//     "price": 15,
//     "description": "Ez egy leiras1",
//     "category": "OTHER"
// }