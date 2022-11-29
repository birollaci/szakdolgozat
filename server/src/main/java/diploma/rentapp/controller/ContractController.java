package diploma.rentapp.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.User;
import diploma.rentapp.service.ContractService;
import diploma.rentapp.service.EmailService;
import diploma.rentapp.service.UserService;

import javax.validation.ValidationException;
@RestController
@RequestMapping(path = "/contract")
@PreAuthorize("isAuthenticated()")
public class ContractController {
    
    Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final UserService userService;
    private final ContractService contractService;
    private final EmailService emailService;

    @Autowired
    public ContractController(UserService userService, ContractService contractService, EmailService emailService) {
        this.userService = userService;
        this.contractService = contractService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<Contract> getContract() {
        Contract contract = getCurrentUserContract();
        logger.info("Visszateritett contract");
        return new ResponseEntity<Contract>(contract, HttpStatus.OK);
    }

    @PostMapping("/rent")
    public ResponseEntity buyContractContent(){
        try {
            Contract contract = getCurrentUserContract();
            logger.info("/rent", contract);
            userService.buyContractContent(contract);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String)auth.getPrincipal();
            User user = userService.getUserByUsername(username);
            emailService.SendSimpleEmail(user.getEmail(), "Rentapp confirmation", "You have successfully rented vehicles!");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<Contract> addVehicleToContract(@PathVariable Long vehicleId){
        Contract contract = getCurrentUserContract();
        contract = contractService.addVehicleToContract(contract, vehicleId);
        return new ResponseEntity<Contract>(contract, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Contract> emptyContract(){
        Contract contract = getCurrentUserContract();
        contract = contractService.emptyContract(contract);
        return new ResponseEntity<Contract>(contract, HttpStatus.OK);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Contract> removeVehicleFromContract(@PathVariable Long vehicleId){
        Contract contract = getCurrentUserContract();
        contract = contractService.removeVehicleFromContract(contract, vehicleId);
        return new ResponseEntity<Contract>(contract, HttpStatus.OK);
    }

    private Contract getCurrentUserContract(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)auth.getPrincipal();
        User user = userService.getUserByUsername(username);
        return user.getContract();
    }
}