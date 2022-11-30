package diploma.rentapp.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diploma.rentapp.domain.Address;
import diploma.rentapp.repository.AddressRepository;

@Service
public class AddressService {
    Logger logger = LoggerFactory.getLogger(AddressService.class);
    
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAddresses() {
        logger.info("Get cimek");
        return addressRepository.findAll();
    }

    public Optional<Address> getAddress(Long addressId) {
        return addressRepository.findById(addressId);
    }
    
    
    public Address getAddressById(Long addressId) {

    Optional<Address> address = addressRepository.findById(addressId);
        if(!address.isPresent()){
            throw new EntityNotFoundException(String.format("User with id %d does not exist in database", addressId));
        }
        return address.get();
    }
    public Address createAddress(Address address){
        return addressRepository.save(address);
    }

    public Address updateAddress(Address address, Address newData){
        address.copyValidValuesFrom(newData);
        return addressRepository.save(address);
    }

    public void deleteAddress(Long addressId){
        addressRepository.deleteById(addressId);
    }
}
