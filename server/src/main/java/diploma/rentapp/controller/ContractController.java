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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.User;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.service.ContractService;
import diploma.rentapp.service.EmailService;
import diploma.rentapp.service.UserService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
        logger.info("Get contract");
        return new ResponseEntity<Contract>(contract, HttpStatus.OK);
    }

    @PostMapping("/rent/{start}/{end}")
    public ResponseEntity rentContractContent(@PathVariable String start, @PathVariable String end) throws ParseException{
        
        Contract contract = getCurrentUserContract();
        List<Vehicle> vehicles = contract.getVehicles();
        if(vehicles.size() > 0) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date startDate = simpleDateFormat.parse(start);
            System.out.println(startDate);
            Date endDate = simpleDateFormat.parse(end);
            System.out.println(endDate);
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String)auth.getPrincipal();
            User user = userService.getUserByUsername(username);
            userService.rentContractContent(contract);
            // Locale locale = new Locale("hu", "HU");
            // DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            // String dateStart = dateFormat.format(startDate);
            // String dateEnd = dateFormat.format(endDate);
    
            String vehiclesStr = "\n";
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            long total = 0;
            for (int i = 0; i < vehicles.size(); i++) {
                vehiclesStr += (i+1) + ". Vehicle: \n";
                vehiclesStr += "Name: " + vehicles.get(i).getName() + "\n";
                vehiclesStr += "Brand: " + vehicles.get(i).getBrand() + "\n";
                vehiclesStr += "Price/day: " + vehicles.get(i).getPrice() + "\n";
                vehiclesStr += "Total price: " + vehicles.get(i).getPrice()*days + "\n";
                vehiclesStr += "Description: " + vehicles.get(i).getName() + "\n";
                vehiclesStr += "Category: " + vehicles.get(i).getName() + "\n\n";
                total += vehicles.get(i).getPrice()*days;
            }
            vehiclesStr += "\nSum: " + total + "\n";
            String textMessage = "You have successfully rented vehicles!\n StartDate: " + start
             + "\n EndDate: " + end + "\n\n Vehicles: " + vehiclesStr;
            emailService.SendSimpleEmail(user.getEmail(), "Rentapp confirmation", textMessage );
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<Contract> addVehicleToContract(@PathVariable Long vehicleId){
        Contract contract = getCurrentUserContract();
        List<Vehicle> vehicles = contract.getVehicles();
        Boolean idGuard = true;
        for (int i = 0; i < vehicles.size(); i++) {
            if(vehicles.get(i).getId() == vehicleId) {
                idGuard = false;
            }
        }
        if(idGuard) {
            contract = contractService.addVehicleToContract(contract, vehicleId);
            return new ResponseEntity<Contract>(contract, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
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