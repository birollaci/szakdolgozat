package diploma.rentapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.Role;
import diploma.rentapp.service.RoleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(path = "/role")
public class RoleController {
    Logger logger = LoggerFactory.getLogger(RoleController.class);
    
    private final RoleService RoleService;

    @Autowired
    public RoleController(RoleService RoleService) {
        this.RoleService = RoleService;
    }

    @GetMapping
    public List<Role> getRoles() {
        logger.info("Visszateritett role-ok");
        return RoleService.getRoles();
    }
}
