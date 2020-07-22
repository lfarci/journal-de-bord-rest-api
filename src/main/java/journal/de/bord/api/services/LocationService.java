package journal.de.bord.api.services;

import journal.de.bord.api.entities.Location;
import org.springframework.stereotype.Service;

public interface LocationService {

    /**
     * Finds a location by id.
     *
     * @param locationId is the location id.
     * @return the location recorded with the given id.
     * @throws IllegalArgumentException when the given id does not match any
     * record.
     */
    Location findById(Long locationId);

    /**
     * Tells if a location with the given id exists.
     *
     * @param locationId is the id of the location.
     * @return true if the location id exists.
     */
    Boolean existsById(Long locationId);

}
