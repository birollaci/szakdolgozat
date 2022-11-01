package diploma.rentapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import diploma.rentapp.domain.ECategory;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.service.VehicleService;

@Configuration
public class VehicleConfig {
    
    Logger logger = LoggerFactory.getLogger(VehicleConfig.class);
    @Bean
    CommandLineRunner vehicleConfigCommandLineRunner(VehicleService vehicleService) {
        
        return args -> {

            // Vehicle vehicle1 = new Vehicle("Kép", 5, "Ez egy szép festmeny.", ECategory.ART);
            // Vehicle vehicle2 = new Vehicle("Jároka", 7, "Ha szüksége lenne egy biztos támaszra a gyereke számára.", ECategory.BABY);
            // Vehicle vehicle3 = new Vehicle("Ecset", 12, "Festőecset, érezze magát Picassonak.", ECategory.ART);
            // Vehicle vehicle4 = new Vehicle("Autó", 3, "Gyorsulás 0-ról 100-ra 5 másodperc alatt. Jó választás.", ECategory.CAR);
            // Vehicle vehicle5 = new Vehicle("Ház", 150, "Nincs hol laknia? Csak tegye be kosarába és minden megoldódik.", ECategory.OTHER);
            // Vehicle vehicle6 = new Vehicle("Motor", 5, "Gyorsulási motor csak saját felelősségre.", ECategory.CAR);
            // Vehicle vehicle7 = new Vehicle("Kosárlabda", 5, "Pattogtasson kedvére sajár otthonában vagy a pályán.", ECategory.SPORTS);
            // vehicleService.createProduct(vehicle1);
            // vehicleService.createProduct(vehicle2);
            // vehicleService.createProduct(vehicle3);
            // vehicleService.createProduct(vehicle4);
            // vehicleService.createProduct(vehicle5);
            // vehicleService.createProduct(vehicle6);
            // vehicleService.createProduct(vehicle7);
            logger.info("Sikeresen feltoltodott");
        };
    }
}
