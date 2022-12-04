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

            Vehicle vehicle1 = new Vehicle("f40", "Ferrari", 5, "Ez egy szép auto.", ECategory.CAR);
            Vehicle vehicle2 = new Vehicle("R50","DHS", 2, "Ez egy szep bicikli.", ECategory.BIKE);
            Vehicle vehicle3 = new Vehicle("Authico", "Spartan", 1, "Ez egy szép roller.", ECategory.ROLLER);
            Vehicle vehicle4 = new Vehicle("Transporter", "Volkswagen", 10, "Gyorsulás 0-ról 100-ra 30 másodperc alatt. Jó választás.", ECategory.VAN);
            Vehicle vehicle5 = new Vehicle("T680", "Kenworth", 20, "Gyorsulás 0-ról 100-ra? Igen.", ECategory.TRUCK);
            Vehicle vehicle6 = new Vehicle("Bözsi", "Ikarus", 5, "Egyik legismertebb és legsikeresebb távolsági- és turistabusz-típusa volt, melyet 1954 és 1973 között gyártottak.", ECategory.BUS);
            Vehicle vehicle7 = new Vehicle("VF750F", "Honda", 5, "Gyorsulási motor csak saját felelősségre.", ECategory.MOTORCYCLE);
            
            vehicleService.createVehicle(vehicle1);
            vehicleService.createVehicle(vehicle2);
            vehicleService.createVehicle(vehicle3);
            vehicleService.createVehicle(vehicle4);
            vehicleService.createVehicle(vehicle5);
            vehicleService.createVehicle(vehicle6);
            vehicleService.createVehicle(vehicle7);

            logger.info("Test vehicle datas created");
            logger.info("The server has started");
        };
    }
}
