package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import pl.com.foks.infrastructure.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final RentalService rentalService;
    private final VehicleService vehicleService;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Gets all currently rented vehicles by user
     * @param userId user id
     * @return list of rented vehicles
     */
    public Optional<Vehicle> getRentedVehicles(String userId) {
        return rentalService.getRentalByUserId(userId)
                .map(rental -> vehicleService.getVehicleById(rental.getVehicleId()));
    }
}
