package diploma.rentapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import diploma.rentapp.domain.Contract;
import diploma.rentapp.domain.ECategory;
import diploma.rentapp.domain.Vehicle;
import diploma.rentapp.repository.ContractRepository;
import diploma.rentapp.repository.VehicleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class VehicleService {
    Logger logger = LoggerFactory.getLogger(VehicleService.class);
    
    private final VehicleRepository vehicleRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, ContractRepository contractRepository) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
    }

    public List<Vehicle> getVehicles() {
        logger.info("Get vehicles");

        return vehicleRepository.findAll();
    }

    public List<Vehicle> getVehiclesByCategory(ECategory category) {
        return vehicleRepository.findAllByCategory(category);
    }

    public Optional<Vehicle> getVehicle(Long vehicleId){
        return vehicleRepository.findById(vehicleId);
    }

    public Vehicle createVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicleById(Long id){
        Vehicle vehicle = vehicleRepository.getById(id);
        List<Contract> contracts = contractRepository.findAllByVehiclesContains(vehicle);
        for (Contract contract : contracts) {
            contract.setVehicles(new ArrayList<Vehicle>());
            contractRepository.save(contract);
        }
        contractRepository.flush();
        vehicleRepository.deleteById(id);
    }
}
