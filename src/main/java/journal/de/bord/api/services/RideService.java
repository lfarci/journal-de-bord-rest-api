package journal.de.bord.api.services;

import journal.de.bord.api.dto.StopDto;
import journal.de.bord.api.entities.Driver;
import journal.de.bord.api.entities.Ride;
import journal.de.bord.api.entities.Stop;

import java.util.List;
import java.util.Optional;

public interface RideService {

    /**
     * Gets a list of rides for the specified driver.
     *
     * @param driver is the driver to get rides for.
     * @param last if specified and true then only the last driver ride is
     * returned.
     * @return the response containing a list of rides.
     * @throws NullPointerException when the driver is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist.
     * @throws IllegalStateException when trying to get the last ride for a
     * driver who hasn't driven before.
     */
    List<Ride> getAllDriverRides(Driver driver, Optional<Boolean> last);

    /**
     * Starts a new ride for the specified driver. A new ride should take place
     * after the last finished ride. Two rides cannot be in progress at the
     * same time.
     *
     * @param driver is the driver to create a new drive for.
     * @param departure is the data describing the departure of the new ride
     * and its driver.
     * @throws NullPointerException when the driver or the departure argument
     * is null.
     * @throws IllegalArgumentException when the specified driver or stop
     * location does not exist.
     * @throws IllegalStateException when the driver is currently driving or
     * when the given departure took place before the last driver arrival.
     */
    void start(Driver driver, StopDto departure);

    /**
     * Updates the specified ride.
     *
     * @param ride is the updated ride.
     * @throws NullPointerException when the ride argument is null.
     */
    void update(Ride ride);

    /**
     * Deletes the specified ride owned by the given driver.
     *
     * @param rideId is the ride id of the entity to be deleted.
     * @throws NullPointerException when the rideId is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist or the ride id doesn't match any records.
     */
    void deleteById(Long rideId);

}
