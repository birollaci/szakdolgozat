package diploma.rentapp.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToMany( fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    public Contract() {
        vehicles = new ArrayList<Vehicle>();
    }

    public Contract(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void copyValidValuesFrom(@NotNull Contract contract) {
        if(contract.vehicles != null){
            this.vehicles = contract.vehicles;
        }
    }

}
