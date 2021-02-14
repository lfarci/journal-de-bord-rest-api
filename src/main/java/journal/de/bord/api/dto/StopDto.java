package journal.de.bord.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopDto {

    /**
     * The moment the stop took place at.
     */
    @NotNull
    private LocalDateTime moment;

    /**
     * This stop location identifier.
     */
    @NotNull
    private Long locationId;

    /**
     * The odometer value displayed by the driver vehicle during this stop.
     */
    @NotNull
    @Min(0)
    private Long odometerValue;

}