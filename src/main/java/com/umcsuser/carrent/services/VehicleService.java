package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    public List<Vehicle> listVehicles(String role) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (role.equals("USER")) {
            vehicles.removeIf(vehicle -> {
                Optional<Rental> rental = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicle.getId());
                return rental.isPresent();
            });
        }
        return vehicles;
    }

    public void rentVehicle(User loggedUser, String vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isPresent()) {
            rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId)
                    .ifPresentOrElse((rental) -> log.warning("Vehicle is already rented"), () -> {
                        Rental rental = new Rental(
                                "",
                                vehicleId,
                                loggedUser.getId(),
                                LocalDateTime.now().toString(),
                                null
                        );
                        rentalRepository.save(rental);
                        log.info("Vehicle rented successfully");
                    });
        } else {
            log.warning("Vehicle not found");
        }
    }

    public void returnVehicle(User loggedUser, String vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isPresent()) {
            rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId)
                    .filter((rental) -> rental.getUserId().equals(loggedUser.getId()))
                    .ifPresentOrElse((rental) -> {
                        rental.setReturnDate(LocalDateTime.now().toString());
                        rentalRepository.save(rental);
                        log.info("Vehicle returned successfully");
                    }, () -> log.warning("You are not the one who rented this vehicle"));
        } else {
            log.warning("Vehicle not found");
        }
    }

    public void addVehicleFromString(String vehicle, Map<String, Object> attributes) {
        Optional<Vehicle> vehicleOptional = stringToVehicle(vehicle, attributes);
        if (vehicleOptional.isEmpty()) {
            log.warning("Invalid vehicle data");
            return;
        }
        vehicleRepository.save(vehicleOptional.get());
        log.info("Vehicle added successfully");
    }

    public void removeVehicle(String vehicleId) {
        vehicleRepository.deleteById(vehicleId);
        log.info("Vehicle removed successfully");
    }

    private Optional<Vehicle> stringToVehicle(String vehicle, Map<String, Object> attributes) {
        String[] parts = vehicle.split(",");
        if (parts.length == 6) {
            return Optional.of(Vehicle.builder()
                    .id("")
                    .category(parts[0])
                    .brand(parts[1])
                    .model(parts[2])
                    .year(Integer.parseInt(parts[3]))
                    .plate(parts[4])
                    .price(Double.parseDouble(parts[5]))
                    .attributes(attributes)
                    .build());
        }
        return Optional.empty();
    }
}
