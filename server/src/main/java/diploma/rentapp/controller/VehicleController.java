package diploma.rentapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.ECategory;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.service.VehicleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/vehicle")
@PreAuthorize("isAuthenticated()")
public class VehicleController {
    Logger logger = LoggerFactory.getLogger(VehicleController.class);
    
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    @GetMapping
    public ResponseEntity<List<Vehicle>> getVehicles() {
        logger.info("Get Vehicles");

        return new ResponseEntity<List<Vehicle>>(vehicleService.getVehicles(), HttpStatus.OK);
    }
    
    @GetMapping("/{vehicleId}")
    public ResponseEntity getVehicleById(@PathVariable("vehicleId") Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleService.getVehicle(vehicleId);
        if(!vehicle.isPresent()){
            logger.warn("Vehicle with " + vehicleId + " is not found");
            return new ResponseEntity<String>(String.format("Vehicle with id '%d' does not exist", vehicleId), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Vehicle>(vehicle.get(), HttpStatus.OK);
    }
    
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity deleteVehicleById(@PathVariable("vehicleId") Long vehicleId) {
        vehicleService.deleteVehicleById(vehicleId);
        logger.warn("The vehicle with " + vehicleId + " deleted");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Vehicle>> getVehiclesByCategory(@PathVariable("category") ECategory category) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByCategory(category);
        logger.info("Get vehicles by category");
        return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.OK);
    }
}
