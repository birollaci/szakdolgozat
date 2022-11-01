package alf.webshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.Role;
import diploma.rentapp.repository.RoleRepository;
import diploma.rentapp.repository.UserRepository;
import diploma.rentapp.service.RoleService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    private Role role = new Role(ERole.ROLE_ADMIN);
    private Role role2 = new Role(ERole.ROLE_USER);

    @BeforeAll
    public void beforeAll() {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @BeforeEach
    public void before() {
        roleRepository.deleteAll();
        roleRepository.flush();
    }

    @Test
    public void roleServiceContextLoads() {
        assertNotNull(roleService);
    }

    @Test
    public void databaseEmpty(){
        boolean roleExist = roleService.getRoles().isEmpty();
        assertTrue(roleExist);
    }

    @Test
    public void createRole(){
        Role result = roleService.createRole(role);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void getRole(){
        roleService.createRole(role);
        roleService.createRole(role2);
        List<Role> roles = roleService.getRoles();
        assertEquals(2, roles.size());
    }

    @Test
    public void deleteRole(){
        Role role3 = roleService.createRole(role);
        roleService.deleteRole(role3);
        List<Role> roles = roleService.getRoles();
        assertEquals(0, roles.size());
    }

}