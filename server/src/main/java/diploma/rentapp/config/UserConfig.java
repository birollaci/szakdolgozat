package diploma.rentapp.config;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import diploma.rentapp.domain.Address;
import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.Role;
import diploma.rentapp.domain.User;
import diploma.rentapp.service.RoleService;
import diploma.rentapp.service.UserService;

@Configuration
public class UserConfig {
    
    Logger logger = LoggerFactory.getLogger(UserConfig.class);
    
    @Bean
    CommandLineRunner userConfigCommandLineRunner(RoleService roleService, UserService userService) {
        
        return args -> {
            
            // save all roles to db
            for (ERole eRole : ERole.values()) {
                try {
                    roleService.createRole(new Role(eRole));
                } catch (EntityExistsException e) {
                    logger.warn(e.getMessage());
                }
            }

            if(!userService.userExistsByRole(ERole.ROLE_ADMIN)){
                User admin = new User(
                    "admin",
                    "admin",
                    "Laci",
                    "Bíró",
                    "admin@webshop.hu",
                    "0036301234567",
                    new Address("Country", "county", "city", "zipCode", "streetName", "1", "door"),
                    null
                );
    
                userService.createAdminUser(admin);
            }

            if(!userService.userExistsByRole(ERole.ROLE_USER)){
                User admin = new User(
                    "user",
                    "user",
                    "Elek",
                    "Teszt",
                    "test@test.hu",
                    "003612346798",
                    new Address("CountryTest", "countytest", "citytest", "zipCodetest", "streetNametest", "2", "doortest"),
                    null
                );
    
                userService.createAdminUser(admin);
            }
        };
    }
}
