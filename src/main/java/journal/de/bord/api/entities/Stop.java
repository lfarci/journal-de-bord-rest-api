package journal.de.bord.api.entities;

import journal.de.bord.api.dto.StopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a stop made by a driver during one of his rides. For now only the ride departure and ride arrival are
 * registered.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stop {

    /**
     * Identifies this entity.
     */
    @Id
    @GeneratedValue(generator = "stop_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "stop_sequence_generator",
            sequenceName = "stop_sequence",
            allocationSize = 1
    )
    private Long id;

    /**
     * The moment the stop took place at.
     */
    @NotNull
    private LocalDateTime moment;

    /**
     * The odometer value displayed by the driver vehicle during this stop.
     */
    @NotNull
    @Min(0)
    private Long odometerValue;

    /**
     * The location where this stop took place.
     */
    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    public Stop(@NotNull LocalDateTime moment, @NotNull @Min(0) Long odometerValue, @Valid @NotNull Location location) {
        this.moment = moment;
        this.odometerValue = odometerValue;
        this.location = location;
    }

    /**
     * Tells if this stop took place after the given stop.
     *
     * @param stop is the stop that took place before this stop.
     * @return true if this stop took place after the given stop. If the given
     * stop is null then false is returned.
     */
    public Boolean isAfter(Stop stop) {
        return stop != null && this.moment.isAfter(stop.moment);
    }

}