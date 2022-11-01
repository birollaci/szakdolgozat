package diploma.rentapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.repository.ContractRepository;
import diploma.rentapp.repository.VehicleRepository;

@Service
public class ContractService {
    Logger logger = LoggerFactory.getLogger(ContractService.class);
    
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, VehicleRepository vehicleRepository) {
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<Contract> getContracts() {
        logger.info("Visszateritett szerzodesek");

        return contractRepository.findAll();
    }

    public Optional<Contract> getContract(Long contractId){
        return contractRepository.findById(contractId);
    }

    public Contract createContract(){
        return contractRepository.save(new Contract());
    }

    public Contract updateContract(Contract contract, Contract newData){
        contract.copyValidValuesFrom(newData);
        return contractRepository.save(contract);
    }

    public Contract addVehicleToContract(Contract contract, Long vehicleId) {
        List<Vehicle> vehicles = contract.getVehicles();
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if(!vehicle.isPresent()){
            throw new EntityNotFoundException(String.format("Vehicle with id %d not found", vehicleId));
        }
        vehicles.add(vehicle.get());
        contract.setVehicles(vehicles);
        return contractRepository.save(contract);
    }

    public Contract emptyContract(Contract contract){
        contract.setVehicles(new ArrayList<Vehicle>());
        return contractRepository.save(contract);
    }

    public Contract removeVehicleFromContract(Contract contract, Long vehicleId) {
        List<Vehicle> vehicles = contract.getVehicles();
        vehicles.removeIf(p -> vehicleId == p.getId());
        contract.setVehicles(vehicles);
        return contractRepository.save(contract);
    }

    public void deleteContract(Long contractId){
        contractRepository.deleteById(contractId);
    }
}
