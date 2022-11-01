package diploma.rentapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
	Optional<Role> findByName(ERole name);
	boolean existsByName(ERole name);
}
