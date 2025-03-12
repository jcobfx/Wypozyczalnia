package pl.com.foks.command;

import pl.com.foks.App;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.vehicle.VehicleRepository;

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
                com.help.accept(null);
                return com.unknown;
            });
    }

    private class Commands {
        private final Map<String, Command> commands;

        private Commands() {
            commands = new HashMap<>();
            putCommand(unknown);
            putCommand(exit);
            putCommand(help);
            putCommand(register);
            putCommand(login);
            putCommand(logout);
            putCommand(user);
            putCommand(vehicles);
            putCommand(rent);
            putCommand(_return);
            putCommand(add);
            putCommand(remove);
            putCommand(users);
        }

        private void putCommand(Command command) {
            commands.put(command.getName(), command);
        }

        private final Command unknown = new Command("unknown") {
            @Override
            public void accept(String[] args) {
                appLogger.info("Unknown command");
            }
        };

        private final Command exit = new Command("exit") {
            @Override
            public void accept(String[] args) {
                app.save();
                System.exit(0);
            }
        };

        private final Command help = new Command("help") {
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

        private final Command register = new Command("register") {
            @Override
            public void accept(String[] strings) {
                if (strings.length != 2) return;

                app.getUserRepository().register(User.Role.CLIENT, strings[0], strings[1]).ifPresentOrElse(
                        user -> {
                            app.setCurrentUser(user);
                            appLogger.info("User registered");
                        },
                        () -> appLogger.warning("Cannot register user")
                );
            }
        };

        private final Command login = new Command("login") {
            @Override
            public void accept(String[] strings) {
                if (strings.length != 2) return;

                app.getUserRepository().getAuthentication().authenticate(strings[0], strings[1]).ifPresentOrElse(
                        user -> {
                            app.setCurrentUser(user);
                            appLogger.info("User logged in");
                        },
                        () -> appLogger.warning("Cannot login")
                );
            }
        };

        private final Command logout = new Command("logout") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                app.setCurrentUser(null);
                appLogger.info("""
                    Register user (register <name> <password>)
                    Login (login <name> <password>)
                    """);
            }
        };

        private final Command user = new Command("user") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                if (currentUser.getRole() == User.Role.ADMIN && strings.length == 1) {
                    appLogger.info(app.getUserRepository().getUser(Integer.parseInt(strings[0])).toString());
                } else {
                    appLogger.info(app.getCurrentUser().toString());
                }
            }
        };

        private final Command vehicles = new Command("vehicles") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null) return;

                appLogger.info(app.getVehicleRepository().getVehicles().toString());
            }
        };

        private final Command rent = new Command("rent") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || strings.length != 1) return;

                final VehicleRepository vehicleRepository = app.getVehicleRepository();
                final int id = Integer.parseInt(strings[0]);
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

        private final Command _return = new Command("return") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || strings.length != 1) return;

                final VehicleRepository vehicleRepository = app.getVehicleRepository();
                final int id = Integer.parseInt(strings[0]);
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

        private final Command add = new Command("add") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN) return;
            }
        };

        private final Command remove = new Command("remove") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN) return;
            }
        };

        private final Command users = new Command("users") {
            @Override
            public void accept(String[] strings) {
                final User currentUser = app.getCurrentUser();
                if (currentUser == null || currentUser.getRole() != User.Role.ADMIN) return;

                appLogger.info(app.getUserRepository().getUsers().toString());
            }
        };
    }
}
