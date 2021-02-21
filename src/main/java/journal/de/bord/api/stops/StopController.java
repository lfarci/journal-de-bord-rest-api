package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.drivers.DriverService;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * The controller handle the REST interface exposing the stops resources.
 */
@RestController
public class StopController {

    private static final String STOPS_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops";
    private static final String STOP_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops/{identifier}";
    private static final String STOP_LOCATION_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops/{identifier}/location";

    @Autowired
    private DriverService driverService;

    @Autowired
    private StopDatabaseTable stopDatabaseTable;

    @Autowired
    private LocationService locationService;

    /**
     * Creates a new stop for the specified driver.
     *
     * The request body content is of JSON type and has the following fields:
     * - "moment": an ISO data string.
     * - "locationId": the id of the location the stop took place at.
     * - "odometerValue": is the vehicle odometer value at the stop.
     *
     * @param pseudonym is the pseudonym of the driver to create a new stop for.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the driver does not exist (404). Or
     * when the provided location id is not found (422). Or when one of the
     * driver stop has already taken place at the given location and moment.
     */
    @PostMapping(path = STOPS_RESOURCE_PATH)
    public ResponseEntity create(
            @PathVariable("pseudonym") String pseudonym,
            @Valid @RequestBody StopDto stop
    ) {
        try {
            Driver driver = driverService.findById(pseudonym);
            Long locationId = stop.getLocationId();
            if (locationService.existsById(locationId)) {
                Location location = locationService.findById(locationId);
                stopDatabaseTable.createNewStopFor(driver, stop, location);
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        }
    }

    /**
     * Gets a specific user's stop.
     *
     * @param pseudonym is the pseudonym of the driver to get the stop for.
     * @param identifier is the stop id.
     * @return the response containing the location (JSON).
     * @throws ResponseStatusException 404 the specified driver or stop id does
     * not exist.
     */
    @GetMapping(path = STOP_RESOURCE_PATH)
    public ResponseEntity stop(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier
    ) {
        try {
            Driver driver = driverService.findById(pseudonym);
            Stop stop = stopDatabaseTable.findStopFor(driver, identifier);
            return ResponseEntity.ok(stop);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets all the stops that a driver has made.
     *
     * @param pseudonym is the pseudonym of the driver to get the stops for.
     * @return the response containing a list of stops.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = STOPS_RESOURCE_PATH)
    public ResponseEntity locations(@PathVariable("pseudonym") String pseudonym) {
        try {
            return ResponseEntity.ok("Get all stops endpoint");
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Replaces the specified stop with the given one.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the stop id.
     * @param data is the data of the new stop.
     * @return the response without content (204).
     * @throws ResponseStatusException when the driver or the stop id do not
     * exist (404). Or when the provided location id is not found (422).
     */
    @PutMapping(path = STOP_RESOURCE_PATH)
    public ResponseEntity update(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody StopDto data
    ) {
        try {
            return ResponseEntity.ok("Update stop endpoint.");
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        }
    }

    /**
     * Deletes the specified stop.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the stop id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the location cannot be
     * found (404).
     */
    @DeleteMapping(path = STOP_RESOURCE_PATH)
    public ResponseEntity delete(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier
    ) {
        try {
            return ResponseEntity.ok("Delete stop with id: " + identifier);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
