package journal.de.bord.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a stop made by a driver during one of his rides. For now only the ride departure and ride arrival are
 * registered.
 */
@Entity
@Data
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

}