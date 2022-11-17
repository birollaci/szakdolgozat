package diploma.rentapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diploma.rentapp.domain.ECategory;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.repository.VehicleRepository;
import diploma.rentapp.service.VehicleService;

@SpringBootTest
public class VehicleServiceTest {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleService vehicleService;
    private Vehicle vehicle1 = new Vehicle(
        "Ferrari 458",
        "Ferrari",
        2000,
        "valami leiras",
        ECategory.CAR
    );
    private Vehicle vehicle2 = new Vehicle(
        "Roller Freestyle",
        "Raven",
        200,
        "valami leiras ami jo",
        ECategory.ROLLER
    );

    @BeforeEach
    public void before(){
        vehicleRepository.deleteAll();
        vehicleRepository.flush();
    }

    @Test
    public void userServiceContextLoads(){
        assertNotNull(vehicleService);
    }

    @Test
    public void databaseEmptyTest(){
        boolean vehicles = vehicleService.getVehicles().isEmpty();
        assertTrue(vehicles);
    }

    @Test
    public void createVehicleTest(){
        Vehicle result = vehicleService.createVehicle(vehicle1);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void getVehicleTest(){
        vehicleService.createVehicle(vehicle1);
        vehicleService.createVehicle(vehicle2);
        List<Vehicle> vehicles = vehicleService.getVehicles();
        assertEquals(2, vehicles.size());
    }

    public void getVehicleTestByCategory(){
        vehicleService.createVehicle(vehicle2);
        List<Vehicle> vehicles = vehicleService.getVehicles();
        assertEquals(ECategory.VAN, vehicles.get(0).getCategory());
    }

    @Test
    public void updateVehicleTest(){
        Vehicle created = vehicleService.createVehicle(vehicle1);
        created = vehicleService.updateVehicle(vehicle2);
        assertNotNull(created);
        assertEquals(created.getId(), vehicle2.getId());
        assertEquals(created.getName(), vehicle2.getName());
    }

    @Test
    public void deleteUserTest(){
        Vehicle vehicle = vehicleService.createVehicle(vehicle1);
        vehicleService.deleteVehicleById(vehicle.getId());
        List<Vehicle> vehicles = vehicleService.getVehicles();
        assertEquals(0, vehicles.size());
    }
}