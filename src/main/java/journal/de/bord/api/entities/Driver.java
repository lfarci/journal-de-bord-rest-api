package journal.de.bord.api.entities;

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

@Entity
@Data
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
    private List<Ride> rides;

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

}