package diploma.rentapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.User;
import diploma.rentapp.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/user")
@PreAuthorize("isAuthenticated()")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<User> getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        User user = userService.getUserByUsername(username);
        logger.info("Response user");
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername((String)auth.getPrincipal());
        user = userService.updateUser(currentUser, user);
        logger.info("User updated");

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<User> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        userService.deleteUserByUsername(username);
        logger.info("User " + username + " deleted");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
