package journal.de.bord.api.rides;

import journal.de.bord.api.stops.StopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    @NotNull
    @Min(0)
    private Long departure;

    /**
     * The arrival is nullable to represent rides that are not finished.
     */
    @Min(0)
    @Nullable
    private Long arrival;

    @Nullable
    private TrafficCondition trafficCondition;

    @Nullable
    private String comment;

}
