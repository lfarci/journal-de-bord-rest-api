package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.locations.LocationDto;

import java.util.List;

public interface StopService {

    /**
     * Finds a stop by id.
     *
     * @param stopId is the stop id.
     * @return the stop recorded with the given id.
     * @throws IllegalArgumentException when the given id does not match any
     * record.
     */
    Location findById(Long stopId);

    /**
     * Gets one of the driver location with the specified id.
     *
     * @param driver is the driver to get a location for.
     * @param identifier is the location id.
     * @return the location with the specified id.
     */
    Location findStopFor(Driver driver, String identifier);

    /**
     * Gets all the locations visited by a driver.
     *
     * @param driver is the driver that visited the locations to find.
     * @return the locations that the given driver visited.
     */
    List<Location> findAllStopsFor(Driver driver);

    /**
     * Tells if a location with the given id exists.
     *
     * @param locationId is the id of the location.
     * @return true if the location id exists.
     */
    Boolean existsById(Long locationId);

    /**
     * Creates a new location.
     *
     * @param driver is the location owner.
     * @param location is the new location.
     * @throws IllegalStateException if the new location name already exists.
     * @throws NullPointerException is one of the argument is null.
     */
    void createNewStopFor(Driver driver, LocationDto location);

    /**
     * Updates the specified location.
     *
     * @param driver is the driver that has visited the location.
     * @param identifier is the id of the location to update.
     * @param data is the updated location.
     * @throws NullPointerException if one of the argument is null.
     * @throws IllegalArgumentException if the given id does not exist.
     * @throws IllegalStateException if the location data is not valid.
     */
    void updateStopFor(Driver driver, String identifier, LocationDto data);

    /**
     * Deletes the specified location.
     *
     * @Driver driver is the driver to delete a location for.
     * @param identifier is the location id of the entity to be deleted.
     * @throws NullPointerException when the identifier is null.
     * @throws IllegalArgumentException when the specified location does not
     * exist.
     * @throws NumberFormatException if the string does not contain a parsable long.
     */
    void deleteStopFor(Driver driver, String identifier);

}
