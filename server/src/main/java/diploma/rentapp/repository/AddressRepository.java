package diploma.rentapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import diploma.rentapp.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}