package diploma.rentapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diploma.rentapp.domain.Address;
import diploma.rentapp.repository.AddressRepository;
import diploma.rentapp.repository.UserRepository;
import diploma.rentapp.service.AddressService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class AddressServiceTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressService addressService;
    private Address address1 = new Address(
            "Romania",
            "Bihar",
            "Varad",
            "415522",
            "Dozsa Gyorgy",
            45,
            "A");

    private Address address2 = new Address(
            "Magyarorszag",
            "Pest",
            "Budapest",
            "1111",
            "Krusper",
            2,
            "B");

    @BeforeAll
    public void beforeAll() {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @BeforeEach
    public void before() {
        addressRepository.deleteAll();
        addressRepository.flush();
    }

    @Test
    public void addressServiceLoadTest(){
        assertNotNull(addressService);
    }

    @Test
    public void databaseisEmptyTest(){
        boolean addressExist = addressService.getAddresses().isEmpty();
        assertTrue(addressExist);
    }


    @Test
    public void createAddressTest(){
        Address result = addressService.createAddress(address2);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void getAddressTest(){
        addressService.createAddress(address1);
        addressService.createAddress(address2);
        List<Address> addresses = addressService.getAddresses();
        assertEquals(2, addresses.size());
    }

    @Test
    public void getAddressByIdTest(){
        Address created = addressService.createAddress(address1);
        Address result = addressService.getAddressById(created.getId());
        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    public void updateAddressTest(){
        Address created = addressService.createAddress(address1);
        Address result = addressService.updateAddress(created, address2);
        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals(created.getStreetName(), result.getStreetName());
    }

    @Test
    public void deleteAddress(){
        Address address = addressService.createAddress(address1);
        addressService.deleteAddress(address.getId());
        List<Address> addresses = addressService.getAddresses();
        assertEquals(0, addresses.size());
    }
}