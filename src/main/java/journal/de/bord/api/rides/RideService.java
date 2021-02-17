package journal.de.bord.api.rides;

import journal.de.bord.api.stops.StopDto;
import journal.de.bord.api.drivers.Driver;

import java.util.List;
import java.util.Optional;

public interface RideService {

    /**
     * Finds a ride by id.
     *
     * @param id is the identifier of the ride to find.
     * @return the ride with the specified id.
     * @throws IllegalArgumentException if the given id doesn't exist or is not a parsable long.
     * @throws NullPointerException if the id is null.
     */
    Ride findById(String id);

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
     * when the given departure took place before the last driver arrival. Also
     * if the departure is not valid.
     */
    void start(Driver driver, StopDto departure);

    /**
     * Updates the specified ride.
     *
     * @param identifier is the id of the ride to update.
     * @param ride is the updated ride.
     * @throws NullPointerException when the ride argument is null.
     * @throws IllegalArgumentException if the given id does not exist.
     * @throws IllegalStateException if the ride data is not valid.
     */
    void update(String identifier, RideDto ride);

    /**
     * Deletes the specified ride owned by the given driver.
     *
     * @param rideId is the ride id of the entity to be deleted.
     * @throws NullPointerException when the rideId is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist or the ride id doesn't match any records.
     * @throws NumberFormatException if the string does not contain a parsable long.
     */
    void deleteById(String rideId);

}
