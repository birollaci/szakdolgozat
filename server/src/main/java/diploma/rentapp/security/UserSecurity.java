package diploma.rentapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import diploma.rentapp.domain.User;
import diploma.rentapp.service.UserService;

@Component("userSecurity")
public class UserSecurity {

    private final UserService userService;

    @Autowired
    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean hasUserId(Authentication authentication, Long userId){
        User user = userService.getUserByUsername((String)authentication.getPrincipal());
        return user.getId() == userId;
    }
}
