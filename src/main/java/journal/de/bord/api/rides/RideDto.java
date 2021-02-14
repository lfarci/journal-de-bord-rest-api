package journal.de.bord.api.rides;

import journal.de.bord.api.stops.StopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
