package com.umcsuser.carrent.app;

import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.RentalJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.UserJdbcRepository;
import com.umcsuser.carrent.repositories.impl.jdbc.VehicleJdbcRepository;
import com.umcsuser.carrent.repositories.impl.json.RentalJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;

public class Main {
    public static void main(String[] args) {
        String storageType;
        if (args.length < 1) {
            storageType = "json";
        } else if (args.length == 1) {
            storageType = args[0];
        } else {
            throw new IllegalArgumentException("Too many arguments");
        }

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        AuthService authService = new AuthService(userRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        App app = new App(authService, vehicleService, rentalService);
        app.run();

    }
}