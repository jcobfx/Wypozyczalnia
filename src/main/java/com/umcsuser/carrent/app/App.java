package com.umcsuser.carrent.app;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Log
public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    private User loggedUser;

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                log.info("Exiting...");
                System.exit(0);
            }

            if (loggedUser == null) {
                switch (input) {
                    case "login": {
                        log.info("Enter login: ");
                        String login = scanner.nextLine();
                        log.info("Enter password: ");
                        String password = scanner.nextLine();
                        authService.login(login, password)
                                .ifPresentOrElse((user) -> {
                                    loggedUser = user;
                                    log.info("Logged in as " + user.getLogin());
                                }, () -> log.info("Invalid login or password"));
                        break;
                    }
                    case "register": {
                        log.info("Enter login: ");
                        String login = scanner.nextLine();
                        log.info("Enter password: ");
                        String password = scanner.nextLine();
                        if (authService.register(login, password, "USER")) {
                            log.info("Registered successfully");
                        } else {
                            log.info("Registration failed");
                        }
                        break;
                    }
                    default:
                        log.info("login or register");
                        break;
                }
            } else {
                switch (input) {
                    case "logout": {
                        loggedUser = null;
                        log.info("Logged out");
                        break;
                    }
                    case "vehicles": {
                        if (loggedUser.getRole().equals("ADMIN")) {
                            log.info("-, add, remove");
                            String cmd = scanner.nextLine();
                            if (cmd.equals("add")) {
                                log.info("Enter vehicle details: ");
                                String details = scanner.nextLine();
                                log.info("Enter additional attributes (key=value): ");
                                String attributes = scanner.nextLine();
                                Map<String, Object> attrMap = getAttrMap(attributes);
                                vehicleService.addVehicleFromString(details, attrMap);
                                break;
                            } else if (cmd.equals("remove")) {
                                log.info("Enter vehicle ID: ");
                                String vehicleId = scanner.next();
                                vehicleService.removeVehicle(vehicleId);
                                break;
                            }
                            log.info("Vehicles:");
                        } else {
                            log.info("Available vehicles:");
                        }
                        vehicleService.listVehicles(loggedUser.getRole()).forEach((v) -> log.info(v.toString()));
                        break;
                    }
                    case "rent": {
                        log.info("Enter vehicle ID: ");
                        String vehicleId = scanner.next();
                        vehicleService.rentVehicle(loggedUser, vehicleId);
                        break;
                    }
                    case "return": {
                        log.info("Enter vehicle ID: ");
                        String vehicleId = scanner.next();
                        vehicleService.returnVehicle(loggedUser, vehicleId);
                        break;
                    }
                    case "rentals": {
                        if (loggedUser.getRole().equals("ADMIN")) {
                            log.info("Enter user ID: ");
                            String userId = scanner.next();
                            rentalService.listRentalsByUserId(userId, loggedUser.getRole())
                                    .forEach((r) -> log.info(r.toString()));
                        } else {
                            rentalService.listRentalsByUserId(loggedUser.getId(), loggedUser.getRole())
                                    .forEach((r) -> log.info(r.toString()));
                        }
                        break;
                    }
                    default:
                        log.info("logout, vehicles, rent, return");
                        break;
                }
            }
        }
    }

    private static Map<String, Object> getAttrMap(String attributes) {
        Map<String, Object> attrMap = new HashMap<>();
        if (!attributes.isBlank()) {
            String[] attrArray = attributes.split(",");
            for (String attr : attrArray) {
                String[] keyValue = attr.split("=");
                if (keyValue.length == 2) {
                    attrMap.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return attrMap;
    }
}
