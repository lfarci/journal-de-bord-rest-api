package farci.logan.jdb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
    @GeneratedValue
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
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_id", referencedColumnName = "id")
    private Stop arrival;

    /**
     * Is the person who was driving the vehicle during this ride.
     */
    @NotNull
    @ManyToOne
    @JoinColumn
    private Driver driver;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TrafficCondition trafficCondition;

    /**
     * The comment can be used by a driver to explain encountered difficulties.
     */
    @Null
    private String comment;

}