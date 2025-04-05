package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.util.List;

public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> listRentalsByUserId(String userId, String role) {
        List<Rental> rentals = rentalRepository.findByUserId(userId);
        if (role.equals("ADMIN")) {
            return rentals;
        } else {
            return rentals.stream()
                    .filter(rental -> rental.getReturnDate() == null)
                    .toList();
        }
    }
}
