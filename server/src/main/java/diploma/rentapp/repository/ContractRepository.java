package diploma.rentapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.Vehicle;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
	List<Contract> findAllByVehiclesContains(Vehicle vehicle);
}