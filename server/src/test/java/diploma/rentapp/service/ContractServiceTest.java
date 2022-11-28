package diploma.rentapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.ECategory;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.repository.ContractRepository;
import diploma.rentapp.repository.VehicleRepository;
import diploma.rentapp.repository.UserRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class ContractServiceTest {
    
    @Autowired
    private ContractRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository productRepository;
    @Autowired
    private ContractService contractService;
   
    /*private Vehicle product2 = new Vehicle(
        "Gep",
        50000,
        "valami leiras ami jo",
        ECategory.ELECTRONICS
    );*/

    ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>() {{
        add(new Vehicle(
            "Ferrari 458",
            "Ferrari",
            2000,
            "valami leiras",
            ECategory.CAR
        )        
    );
        add(new Vehicle(
            "Roller Freestyle",
            "Raven",
            200,
            "valami leiras ami jo",
            ECategory.ROLLER
        ));}
    };

    // 2023 01 01
    Date startDate = new Date(1672531200);

    // 2023 01 02
    Date endDate = new Date(1672617600);

    Contract megaContract = new Contract(vehicles, startDate, endDate);

    @BeforeAll
    public void beforeAll() {
        userRepository.deleteAll();
        userRepository.flush();
    }
    
    @BeforeEach
    public void before() {
        cartRepository.deleteAll();
        cartRepository.flush();
    }

    @Test
    public void contractServiceContextLoad(){
        assertNotNull(contractService);
    }

    @Test
    public void databaseEmpty(){
        boolean cartExist = contractService.getContracts().isEmpty();
        assertTrue(cartExist);
    }


    @Test
    public void createContract() {
        Contract result = contractService.createContract();
        assertNotNull(result.getId());
    }

    @Test
    public void getAddress(){
        contractService.createContract();
        contractService.createContract();
        List<Contract> carts = contractService.getContracts();
        assertEquals(2, carts.size());
    }

    @Test
    public void updateContract(){
        // create vehicles for test
        for (Vehicle product : vehicles) {
            productRepository.save(product);
        }
        productRepository.flush();
        Contract created = contractService.createContract();
        Contract result = contractService.updateContract(created, megaContract);
        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals(created.getVehicles().size(), result.getVehicles().size());
    }

    @Test
    public void deleteContract(){
        Contract cart = contractService.createContract();
        contractService.deleteContract(cart.getId());;
        ArrayList<Contract> carts = (ArrayList)contractService.getContracts();
        assertEquals(0, carts.size());
    }
}