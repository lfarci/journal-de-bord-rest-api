package journal.de.bord.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a journey made with a car by a driver from one stop (departure) to an other (arrival). The driver can
 * specify the traffic condition and tell the system the difficulties he has encountered.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"departure_id", "arrival_id"})})
@Data
@NoArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(generator = "ride_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "ride_sequence_generator",
            sequenceName = "ride_sequence",
            allocationSize = 1
    )
    private Long id;

    /**
     * Is the stop that starts this ride. This stop should be different than the arrival.
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departure_id", referencedColumnName = "id")
    private Stop departure;

    /**
     * Is the stop that ends this ride. This stop should be different than the departure.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_id", referencedColumnName = "id")
    private Stop arrival;

    /**
     * Is the person who was driving the vehicle during this ride.
     */
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Driver driver;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TrafficCondition trafficCondition;

    /**
     * The comment can be used by a driver to explain encountered difficulties.
     */
    private String comment;

    public Ride(@NotNull Stop departure, @NotNull Driver driver) {
        this.departure = departure;
        this.arrival = null;
        this.driver = driver;
        this.trafficCondition = TrafficCondition.NORMAL;
        this.comment = null;
    }

    /**
     * Tells if this ride is done. A ride is done when the driver is arrived to its destination.
     *
     * @return true if the ride is done.
     */
    public Boolean isDone() {
        return arrival != null;
    }

    public LocalDateTime getDepartureMoment() {
        return departure.getMoment();
    }

}