package diploma.rentapp.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.User;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.service.VehicleService;
import diploma.rentapp.service.UserService;


@RestController
@RequestMapping(path = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;
    private final VehicleService vehicleService;

    @Autowired
    public AdminController(UserService userService, VehicleService vehicleService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<User> getUsers() {
        logger.info("Get users");
        return userService.getUsers();
        
    }

    @PostMapping("/createuser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        user = userService.createUser(user);
        logger.info("Created user");
        return new ResponseEntity<User>(user, HttpStatus.CREATED);

    }

    @PostMapping("/createadmin")
    public ResponseEntity<User> createAdminUser(@RequestBody User adminUser){
        adminUser = userService.createAdminUser(adminUser);
        logger.info("Created admin");

        return new ResponseEntity<User>(adminUser, HttpStatus.CREATED);
    }

    @GetMapping("/vehicle")
    public ResponseEntity<List<Vehicle>> getVehicles(){
        List<Vehicle> vehicles = vehicleService.getVehicles();
        logger.info("Get vehicles");
        return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.OK);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable("vehicleId") Long vehicleId){
        Optional<Vehicle> vehicle = vehicleService.getVehicle(vehicleId);
        if(!vehicle.isPresent()){
            logger.warn("There is no such vehicle");
            return new ResponseEntity<Vehicle>((Vehicle)null, HttpStatus.NOT_FOUND);
        }
        logger.info("There is such a vehicle");
        return new ResponseEntity<Vehicle>(vehicle.get(), HttpStatus.OK);
    }

    @PostMapping("/vehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle){
        vehicle = vehicleService.createVehicle(vehicle);
        logger.info("Created a vehicle");

        return new ResponseEntity<Vehicle>(vehicle, HttpStatus.CREATED);
    }
    
    @PutMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable("vehicleId") Long vehicleId){
        vehicleService.deleteVehicleById(vehicleId);
        logger.info("Deleted a vehicle");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
