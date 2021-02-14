package journal.de.bord.api.locations;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.drivers.DriverDatabaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

/**
 * The controller handle the REST interface exposing the locations resources.
 */
@RestController
public class LocationsRestController {

    private static final String LOCATIONS_RESOURCE_PATH = "/api/drivers/{pseudonym}/locations";
    private static final String LOCATION_RESOURCE_PATH = "/api/drivers/{pseudonym}/locations/{identifier}";

    @Autowired
    private DriverDatabaseTable driverDatabaseTable;

    @Autowired
    private LocationDatabaseTable locationDatabaseTable;

    /**
     * Creates a new location for the specified driver.
     *
     * The request body content is of JSON type and has the following fields:
     * - "name": a unique string describing the location.
     * - "latitude": a double.
     * - "longitude": a double.
     *
     * @param pseudonym is the pseudonym of the driver to create a new location for.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the driver does not exist (404).
     * Also throws a 422 if the new location name already exists.
     */
    @PostMapping(path = LOCATIONS_RESOURCE_PATH)
    public ResponseEntity create(
            @PathVariable("pseudonym") String pseudonym,
            @Valid @RequestBody LocationDto location
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            locationDatabaseTable.createNewLocationFor(driver, location);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        }
    }

    /**
     * Gets a specific user location.
     *
     * @param pseudonym is the pseudonym of the driver to get the locations for.
     * @param identifier is the location id.
     * @return the response containing the location.
     * @throws ResponseStatusException 404 the specified driver or location does not exist.
     */
    @GetMapping(path = LOCATION_RESOURCE_PATH)
    public ResponseEntity location(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            Location location = locationDatabaseTable.findLocationFor(driver, identifier);
            return ResponseEntity.ok(location);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets all the locations that a driver has visited.
     *
     * @param pseudonym is the pseudonym of the driver to get the locations for.
     * @return the response containing a list of locations.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = LOCATIONS_RESOURCE_PATH)
    public ResponseEntity locations(@PathVariable("pseudonym") String pseudonym) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            List<Location> locations = locationDatabaseTable.findAllLocationsFor(driver);
            return ResponseEntity.ok(locations);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Replaces the specified location with the given one.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the location id.
     * @param data is the data of the new location.
     * @return the response without content (204).
     * @throws ResponseStatusException if the identifier or the data is not valid.
     */
    @PutMapping(path = LOCATION_RESOURCE_PATH)
    public ResponseEntity update(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody LocationDto data
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            locationDatabaseTable.updateLocationFor(driver, identifier, data);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Deletes the specified location.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the location id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the location cannot be found (404).
     */
    @DeleteMapping(path = LOCATION_RESOURCE_PATH)
    public ResponseEntity delete(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            locationDatabaseTable.deleteLocationFor(driver, identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
