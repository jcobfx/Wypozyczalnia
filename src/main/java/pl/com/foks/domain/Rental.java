package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    private String id;
    private String vehicleId;
    private String userId;
    private String rentDate;
    private String returnDate;
}
