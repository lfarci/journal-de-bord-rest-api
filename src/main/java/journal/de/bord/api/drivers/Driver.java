package journal.de.bord.api.drivers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.rides.Ride;
import journal.de.bord.api.stops.Stop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    private static Optional<Ride> getLastRide(List<Ride> rides) {
        return rides.stream().max(Comparator.comparing(Ride::getDepartureMoment));
    }

    @Id
    private String pseudonym;

    @NotNull
    @Min(0)
    private Long objective;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Ride> rides;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Location> locations;

    public Boolean hasDriven() {
        return !rides.isEmpty();
    }

    public Boolean isDriving() {
        Optional<Ride> lastDrive = Driver.getLastRide(rides);
        return lastDrive.isPresent() && !lastDrive.get().isDone();
    }

    public Optional<Ride> getLastRide() {
        return Driver.getLastRide(rides);
    }

    /**
     * Tells if this driver can start a new ride with the given stop. A driver
     * can start a new ride in if one of the following is true:
     * - The driver hasn't driven before.
     * - The driver is not driving.
     * - The driver last arrival took place before the given stop.
     *
     * @param stop is the stop that this driver can start with.
     * @return true if this driver can start with the given stop.
     */
    public Boolean canStartWith(Stop stop) {
        return stop != null
                && !hasDriven()
                || (!isDriving() && isStopAfterLastRideArrival(stop));
    }

    private Boolean isStopAfterLastRideArrival(Stop stop) {
        Optional<Ride> lastRide = getLastRide();
        return stop != null
                && lastRide.isPresent()
                && stop.isAfter(lastRide.get().getArrival());
    }

    public Location getLocationById(Long identifier) {
        List<Location> results = locations.stream()
                .filter(l -> l.getId().equals(identifier))
                .collect(Collectors.toList());
        return results.isEmpty() ? null : results.get(0);
    }

}