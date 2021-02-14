package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.stops.Stop;
import journal.de.bord.api.stops.StopDto;

import java.util.List;

public interface StopService {

    /**
     * Gets one of the driver Stop with the specified id.
     *
     * @param driver is the driver to get a Stop for.
     * @param identifier is the Stop id.
     * @return the Stop with the specified id.
     */
    Stop findStopFor(Driver driver, String identifier);

    /**
     * Creates a new Stop.
     *
     * @param driver is the Stop owner.
     * @param stop is the new Stop.
     * @param location is the location associated with the stop to create.
     * @throws IllegalStateException if the new Stop name already exists.
     * @throws NullPointerException is one of the argument is null.
     */
    void createNewStopFor(Driver driver, StopDto stop, Location location);

    /**
     * Updates the specified Stop.
     *
     * @param driver is the driver that has visited the Stop.
     * @param identifier is the id of the Stop to update.
     * @param data is the updated Stop.
     * @throws NullPointerException if one of the argument is null.
     * @throws IllegalArgumentException if the given id does not exist.
     * @throws IllegalStateException if the Stop data is not valid.
     */
    void updateStopFor(Driver driver, String identifier, StopDto data);

    /**
     * Deletes the specified Stop.
     *
     * @Driver driver is the driver to delete a Stop for.
     * @param identifier is the Stop id of the entity to be deleted.
     * @throws NullPointerException when the identifier is null.
     * @throws IllegalArgumentException when the specified Stop does not
     * exist.
     * @throws NumberFormatException if the string does not contain a parsable long.
     */
    void deleteStopFor(Driver driver, String identifier);

}
