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
@RequestMapping(path = "/product")
@PreAuthorize("isAuthenticated()")
public class VehicleController {
    Logger logger = LoggerFactory.getLogger(VehicleController.class);
    
    private final VehicleService productService;

    @Autowired
    public VehicleController(VehicleService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<Vehicle>> getVehicles() {
        logger.info("Visszateritett termekek");

        return new ResponseEntity<List<Vehicle>>(productService.getVehicles(), HttpStatus.OK);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity getVehicleById(@PathVariable("productId") Long productId) {
        Optional<Vehicle> product = productService.getVehicle(productId);
        if(!product.isPresent()){
            logger.warn("Nincs ilyen termek");
            return new ResponseEntity<String>(String.format("Vehicle with id '%d' does not exist", productId), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Vehicle>(product.get(), HttpStatus.OK);
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity deleteVehicleById(@PathVariable("productId") Long productId) {
        productService.deleteVehicleById(productId);
        logger.warn("Kitorlodott sikeresen a termek");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Vehicle>> getVehiclesByCategory(@PathVariable("category") ECategory category) {
        List<Vehicle> products = productService.getVehiclesByCategory(category);
        logger.info("Sikeresen visszaterultek a termekek kategoria szerin");
        return new ResponseEntity<List<Vehicle>>(products, HttpStatus.OK);
    }
}
