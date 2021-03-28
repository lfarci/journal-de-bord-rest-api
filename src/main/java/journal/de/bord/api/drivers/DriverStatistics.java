package journal.de.bord.api.drivers;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class DriverStatistics {

    @PositiveOrZero
    private Long rides;

    @PositiveOrZero
    private Long locations;

    @PositiveOrZero
    private Long totalDistance;

}