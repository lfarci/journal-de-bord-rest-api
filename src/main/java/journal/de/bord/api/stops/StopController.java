package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.drivers.DriverService;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * The controller handle the REST interface exposing the stops resources.
 */
@RestController
@RequestMapping("/api/drivers/{driverId}")
public class StopController {

    private static final String STOPS_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops";
    private static final String STOP_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops/{identifier}";
    private static final String STOP_LOCATION_RESOURCE_PATH = "/api/drivers/{pseudonym}/stops/{identifier}/location";

    @Autowired
    private DriverService driverService;

    @Autowired
    private StopService stopService;

    @Autowired
    private LocationService locationService;

    /**
     * Creates a new stop for the specified driver.
     *
     * @param driverId is the identifier of the driver to create a new stop for.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the stop cannot be created.
     */
    @PostMapping(path = "/stops")
    public ResponseEntity create(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @Valid @RequestBody StopDto stop
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            if (locationService.existsById(stop.getLocationId())) {
                Location location = locationService.findById(stop.getLocationId());
                stopService.createNewStopFor(driver, stop, location);
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }

    /**
     * Gets a specific user's stop.
     *
     * @param driverId is the identifier of the driver to get the stop for.
     * @param identifier is the stop id.
     * @return the response containing the location (JSON).
     * @throws ResponseStatusException 404 the specified driver or stop id does
     * not exist.
     */
    @GetMapping(path = "/stops/{identifier}")
    public ResponseEntity stop(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            Stop stop = stopService.findStopFor(driver, identifier);
            return ResponseEntity.ok(stop);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets all the stops that a driver has made.
     *
     * @param driverId is the identifier of the driver to get the stops for.
     * @return the response containing a list of stops.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = "/stops")
    public ResponseEntity stops(
            Authentication user,
            @PathVariable("driverId") String driverId
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            return ResponseEntity.ok(stopService.findAllStopsFor(driver));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    /**
     * Updates the specified stop with the given one.
     *
     * @param driverId is the identifier of the driver.
     * @param identifier is the stop id.
     * @param data is the data of the new stop.
     * @return the response without content (204).
     * @throws ResponseStatusException when the driver or the stop id do not
     * exist (404). Or when the provided location id is not found (422).
     */
    @PutMapping(path = STOP_RESOURCE_PATH)
    public ResponseEntity update(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody StopDto data
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            if (locationService.existsById(data.getLocationId())) {
                Location location = locationService.findById(data.getLocationId());
                stopService.updateStopFor(identifier, driver, data, location);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        }
    }

    /**
     * Deletes the specified stop.
     *
     * @param driverId is the identifier of the driver.
     * @param identifier is the stop id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the location cannot be
     * found (404).
     */
    @DeleteMapping(path = STOP_RESOURCE_PATH)
    public ResponseEntity delete(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            stopService.deleteStopFor(driver, identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private static void requireAuthenticatedOwner(String authenticated, String requested) {
        if (!authenticated.equals(requested)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
