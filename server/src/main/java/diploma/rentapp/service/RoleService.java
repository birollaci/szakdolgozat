package diploma.rentapp.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.Role;
import diploma.rentapp.repository.RoleRepository;

@Service
public class RoleService {
    Logger logger = LoggerFactory.getLogger(RoleService.class);
    
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRole(ERole role){
        return roleRepository.findByName(role);
    }

    public Role createRole(Role role) throws EntityExistsException {
        if(roleRepository.existsByName(role.getName())){
            logger.warn("Role already exists!");
            throw new EntityExistsException(String.format("Role %s already exists, creation aborted", role.getName()));
        }
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Role role){
        roleRepository.delete(role);
    }
}
