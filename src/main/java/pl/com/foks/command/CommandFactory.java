package pl.com.foks.command;

import pl.com.foks.App;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.vehicle.VehicleFactory;
import pl.com.foks.repository.vehicle.VehicleRepository;
import pl.com.foks.repository.vehicle.vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class CommandFactory {
    private static final Logger appLogger = Logger.getLogger(App.class.getName());
    private final App app;
    private final Commands com;

    public CommandFactory(App app) {
        this.app = app;
        this.com = new Commands();
    }

    public Command createCommand(String commandName) {
        return Optional.ofNullable(com.commands.get(commandName.toLowerCase())).
            orElseGet(() -> {
                com.HELP.accept(null);
                return com.UNKNOWN;
            });
    }

    @SuppressWarnings("FieldCanBeLocal")
    private class Commands {
        private final Map<String, Command> commands;

        private Commands() {
            commands = new HashMap<>();
            putCommand(UNKNOWN);
            putCommand(EXIT);
            putCommand(HELP);
            putCommand(REGISTER);
            putCommand(LOGIN);
            putCommand(LOGOUT);
            putCommand(USER);
            putCommand(VEHICLES);
            putCommand(RENT);
            putCommand(RETURN);
            putCommand(ADD);
            putCommand(REMOVE);
            putCommand(USERS);
        }

        private void putCommand(Command command) {
            commands.put(command.getName(), command);
        }

        private final Command UNKNOWN = new Command("unknown") {
            @Override
            public void accept(String[] args) {
                appLogger.info("Unknown command");
            }
        };

        private final Command EXIT = new Command("exit") {
            @Override
            public void accept(String[] args) {
                app.save();
                System.exit(0);
            }
        };

        private final Command HELP = new Command("help") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser != null) {
                    appLogger.info("Available commands: help, exit, logout, user, vehicles, rent <vehicleId>, return <vehicleId>");
                    if (currentUser.getRole() == User.Role.ADMIN) {
                        appLogger.info("Admin commands: add, remove, users, user <userId>");
                    }
                } else {
                    appLogger.info("Available commands: help, register <name> <password>, login <name> <password>, exit");
                }
            }
        };

        private final Command REGISTER = new Command("register") {
            @Override
            public void accept(String[] args) {
                if (args.length != 2) return;

                app.getUserRepository().register(User.Role.CLIENT, args[0], args[1]).ifPresentOrElse(
                        user -> {
                            app.setCurrentUser(user);
                            appLogger.info("User registered");
                        },
                        () -> appLogger.warning("Cannot register user")
                );
            }
        };

        private final Command LOGIN = new Command("login") {
            @Override
            public void accept(String[] args) {
                if (args.length != 2) return;

                app.getUserRepository().getAuthentication().authenticate(args[0], args[1]).ifPresentOrElse(
                        user -> {
                            app.setCurrentUser(user);
                            appLogger.info("User logged in");
                        },
                        () -> appLogger.warning("Cannot login")
                );
            }
        };

        private final Command LOGOUT = new Command("logout") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                app.setCurrentUser(null);
                appLogger.info("""
                    Register user (register <name> <password>)
                    Login (login <name> <password>)
                    """);
            }
        };

        private final Command USER = new Command("user") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                if (currentUser.getRole() == User.Role.ADMIN && args.length == 1) {
                    appLogger.info(app.getUserRepository().getUser(Integer.parseInt(args[0])).toString());
                } else {
                    appLogger.info(app.getCurrentUser().toString());
                }
            }
        };

        private final Command VEHICLES = new Command("vehicles") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                appLogger.info(app.getVehicleRepository().getVehicles().toString());
            }
        };

        private final Command RENT = new Command("rent") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || args.length != 1) return;

                final VehicleRepository vehicleRepository = app.getVehicleRepository();
                final int id = Integer.parseInt(args[0]);
                if (vehicleRepository.getIdentifiers().contains(id)) {
                    if (vehicleRepository.rentVehicle(currentUser, id)) {
                        appLogger.info("Vehicle rented");
                    } else {
                        appLogger.info("Cannot rent vehicle");
                    }
                } else {
                    appLogger.warning("Vehicle not found");
                }
            }
        };

        private final Command RETURN = new Command("return") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || args.length != 1) return;

                final VehicleRepository vehicleRepository = app.getVehicleRepository();
                final int id = Integer.parseInt(args[0]);
                if (vehicleRepository.getIdentifiers().contains(id)) {
                    if (vehicleRepository.returnVehicle(currentUser, id)) {
                        appLogger.info("Vehicle returned");
                    } else {
                        appLogger.info("Cannot return vehicle");
                    }
                } else {
                    appLogger.warning("Vehicle not found");
                }
            }
        };

        private final Command ADD = new Command("add") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN || args.length < 7) return;

                final Vehicle vehicle;
                try {
                    vehicle = VehicleFactory.createVehicle(args);
                    if (app.getVehicleRepository().addVehicle(vehicle)) {
                        appLogger.info("Vehicle added");
                    } else {
                        appLogger.warning("Cannot add vehicle");
                    };
                } catch (RuntimeException e) {
                    appLogger.warning("Cannot add vehicle");
                }
            }
        };

        private final Command REMOVE = new Command("remove") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN || args.length != 1) return;

                if (app.getVehicleRepository().removeVehicle(Integer.parseInt(args[0]))) {
                    appLogger.info("Vehicle removed");
                } else {
                    appLogger.warning("Cannot remove vehicle");
                }
            }
        };

        private final Command USERS = new Command("users") {
            @Override
            public void accept(String[] args) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN) return;

                appLogger.info(app.getUserRepository().getUsers().toString());
            }
        };
    }
}
