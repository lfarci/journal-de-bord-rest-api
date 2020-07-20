package farci.logan.jdb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    @GeneratedValue
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