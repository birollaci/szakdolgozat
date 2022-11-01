package diploma.rentapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.User;
import diploma.rentapp.service.UserService;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/")
public class LoginController {
    
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        try {
            return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
        } catch (ValidationException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
