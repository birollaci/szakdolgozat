package diploma.rentapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import diploma.rentapp.domain.Address;
import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.ERole;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.domain.Role;
import diploma.rentapp.domain.User;
import diploma.rentapp.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final ContractService contractService;
    private final AddressService addressService;
    private final RoleService roleService;
    private final VehicleService vehicleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ContractService contractService, AddressService addressService, RoleService roleService, VehicleService vehicleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.contractService = contractService;
        this.addressService = addressService;
        this.roleService = roleService;
        this.vehicleService = vehicleService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userExistsByRole(ERole eRole){
        Optional<Role> role = roleService.getRole(eRole);
        HashSet<Role> roleSet = new HashSet<Role>(
            Arrays.asList(role.get())
        );
        return userRepository.existsByRolesIn(roleSet);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(String.format("User with id %d does not exist in database", userId));
        }
        return user.get();
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            logger.warn("Mar letezik ilyen");
            throw new EntityNotFoundException(String.format("User with username %s does not exist in database", username));
        }
        return user.get();
    }

    public User createUser(User user){
        return createUserByRole(user, ERole.ROLE_USER);
    }

    public User createAdminUser(User user){
        return createUserByRole(user, ERole.ROLE_ADMIN);
    }

    public void rentContractContent(Contract contract) {
        List<Vehicle> vehicles = contract.getVehicles();
       // contractService.updateContract(contract);
        for (Vehicle product : vehicles) {
            vehicleService.deleteVehicleById(product.getId());
        }
        contract.setVehicles(new ArrayList<Vehicle>());
        contract.setStartDate(null);
        contract.setEndDate(null);
        contractService.updateContract(contract, contract);
    }

    public User updateUser(User user, User newData){
        user.copyValidValuesFrom(newData);
        if(newData.getPassword() != null)
            user.setPassword(passwordEncoder.encode(newData.getPassword()));
        if(newData.getHomeAddress() != null){
            addressService.updateAddress(user.getHomeAddress(), newData.getHomeAddress());
        }
        if(newData.getContract() != null){
            contractService.updateContract(user.getContract(), newData.getContract());
        }
        if(newData.getBillingAddress() != null){
            if(newData.getBillingAddress() != null) {
                if(user.getBillingAddress() == null) { // if user didnt have billing address before, create one
                    Address address = addressService.createAddress(newData.getBillingAddress());
                    user.setBillingAddress(address);
                }
                else {
                    addressService.updateAddress(user.getBillingAddress(), newData.getBillingAddress());
                }
            }
        }
        else {
            if(user.getBillingAddress() != null) {
                Address billingAddress = user.getBillingAddress();
                user.setBillingAddress(null);
                user = userRepository.save(user);
                addressService.deleteAddress(billingAddress.getId());
                return user;
            }
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException(String.format("User with id %d does not exist in database", userId));
        }
        userRepository.deleteById(userId);
    }

    public void deleteUserByUsername(String username){
        if(!userRepository.existsByUsername(username)){
            throw new EntityNotFoundException(String.format("User with username %s does not exist in database", username));
        }
        userRepository.deleteByUsername(username);
    }

    private User createUserByRole(User user, ERole eRole){
        // Check if user would be unique
        if(userRepository.existsByUsername(user.getUsername())){
            throw new EntityExistsException(String.format("User with username %s already exists", user.getUsername()));
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EntityExistsException(String.format("User with email %s already exists", user.getEmail()));
        }
        // set address
        Address homAddress = addressService.createAddress(user.getHomeAddress());
        user.setHomeAddress(homAddress);
        // set billing address
        if(user.getBillingAddress() != null) {
            Address billingAddress = addressService.createAddress(user.getBillingAddress());
            user.setBillingAddress(billingAddress);
        }
        // set contract
        Contract contract = contractService.createContract();
        user.setContract(contract);
        // set role
        Optional<Role> role = roleService.getRole(eRole);
        user.setRoles(new HashSet<Role>(
            Arrays.asList(role.get())
        ));
        // encrypt password
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        // save user
        return userRepository.save(user);
    }
}
