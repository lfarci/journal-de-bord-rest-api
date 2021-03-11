package journal.de.bord.api.locations;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.drivers.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

/**
 * The controller handle the REST interface exposing the locations resources.
 */
@RestController
@RequestMapping("/api/drivers/{driverId}")
public class LocationController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private LocationService locationService;

    /**
     * Creates a new location for the specified driver.
     *
     * @param driverId is the pseudonym of the driver to create a new location for.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the location cannot be created.
     */
    @PostMapping(path = "/locations")
    public ResponseEntity<Object> create(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @Valid @RequestBody LocationDto location
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            Long id = locationService.createNewLocationFor(driver, location);
            return new ResponseEntity(new Object() {
                public final Long locationId = id;
            }, HttpStatus.CREATED);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Gets all the locations that a driver has visited.
     *
     * @param driverId is the identifier of the driver to get the locations for.
     * @return the response containing a list of locations.
     * @throws ResponseStatusException when the specified driver does not exist.
     */
    @GetMapping(path = "/locations")
    public ResponseEntity locations(
            Authentication user,
            @PathVariable("driverId") String driverId
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            List<Location> locations = locationService.findAllLocationsFor(driver);
            return ResponseEntity.ok(locations);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets a specific user location.
     *
     * @param driverId is the identifier of the driver to get a location for.
     * @param identifier is the location id.
     * @return the response containing the location.
     * @throws ResponseStatusException when the driver or the asked location
     * does not exist.
     */
    @GetMapping(path = "/locations/{identifier}")
    public ResponseEntity location(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            Location location = locationService.findLocationFor(driver, identifier);
            return ResponseEntity.ok(location);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Updates the specified location data.
     *
     * @param driverId is the identifier of the driver.
     * @param identifier is the location id.
     * @param data is the data of the new location.
     * @return the response without content (204).
     * @throws ResponseStatusException when the location cannot be updated.
     */
    @PutMapping(path = "/locations/{identifier}")
    public ResponseEntity update(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody LocationDto data
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            locationService.updateLocationFor(driver, identifier, data);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Deletes the specified location.
     *
     * @param driverId is the identifier of the driver.
     * @param identifier is the location id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the location cannot be
     * found (404). Or when the location is referenced by one of the driver's
     * stop (409).
     */
    @DeleteMapping(path = "/locations/{identifier}")
    public ResponseEntity delete(
            Authentication user,
            @PathVariable("driverId") String driverId,
            @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            locationService.deleteLocationFor(driver, identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private static void requireAuthenticatedOwner(String authenticated, String requested) {
        if (!authenticated.equals(requested)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
