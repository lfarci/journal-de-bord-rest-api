package journal.de.bord.api.dto;

import journal.de.bord.api.entities.TrafficCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    @NotNull
    private Long rideId;

    @NotNull
    private StopDto departure;

    private StopDto arrival;

    @NotNull
    @NotBlank
    private String driverPseudonym;

    @NotNull
    private TrafficCondition trafficCondition;

    private String comment;

}
