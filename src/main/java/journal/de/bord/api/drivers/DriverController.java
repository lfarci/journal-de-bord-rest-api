package journal.de.bord.api.drivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * The controller handles the REST interface exposing the drivers resources.
 */
@RestController
public class DriverController {

    private static final String DRIVERS_RESOURCE_PATH = "/api/drivers";
    private static final String DRIVER_RESOURCE_PATH = "/api/drivers/{identifier}";

    @Autowired
    private DriverService driverService;

    /**
     * Creates a new driver. When the driver cannot be created one the
     * following status code are thrown.
     *
     * @param data is the new driver's data.
     * @return created status (201).
     * @throws ResponseStatusException when the driver could not be created.
     */
    @PostMapping(path = DRIVERS_RESOURCE_PATH)
    public ResponseEntity create(
            Authentication user,
            @Valid @RequestBody DriverDto data
    ) {
        try {
            if (data.hasIdentifier() && !isOwner(user.getName(), data)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            data.setIdentifier(user.getName());
            driverService.create(data);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Gets a specific driver.
     *
     * @param identifier is the identifier of the driver to get.
     * @return the response containing the driver.
     * @throws ResponseStatusException 404 when the specified driver identifier
     * could not be found.
     */
    @GetMapping(path = DRIVER_RESOURCE_PATH)
    public ResponseEntity driver(
            Authentication authentication,
            @PathVariable("identifier") String identifier
    ) {
        try {
            String userId = authentication.getName();
            if (userId.equals(identifier)) {
                return ResponseEntity.ok(driverService.findById(identifier));
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted to the owner.");
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets a specific driver.
     *
     * @param identifier is the identifier of the driver to get.
     * @return the response containing the driver.
     * @throws ResponseStatusException 404 when the specified driver identifier
     * could not be found.
     */
    @GetMapping(path = "/api/drivers/{identifier}/statistics")
    public ResponseEntity statistics(
            Authentication authentication,
            @PathVariable("identifier") String identifier
    ) {
        try {
            String userId = authentication.getName();
            if (userId.equals(identifier)) {
                Long count = driverService.countDriverRides(identifier);
                return ResponseEntity.ok(count);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted to the owner.");
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Gets all the drivers.
     *
     * @return the response containing a list of locations.
     */
    @GetMapping(path = DRIVERS_RESOURCE_PATH)
    public ResponseEntity drivers() {
        return ResponseEntity.ok(driverService.findAll());
    }

    /**
     * Replaces the specified location with the given one.
     *
     * @param identifier is the driver id.
     * @return the response without content (204).
     * @throws ResponseStatusException when the identifier or the driver is
     * unknown (404). Or when the body contains a valid driver with a name
     * that already exist (409). Or when there is a mismatch between the URI
     * identifier and the data identifier (403).
     */
    @PutMapping(path = DRIVER_RESOURCE_PATH)
    public ResponseEntity update(
            Authentication user,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody DriverDto data
    ) {
        try {
            if (data.hasIdentifier() && !isOwner(user.getName(), data)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!identifier.equals(user.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            driverService.update(data);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Deletes the specified driver.
     *
     * @param identifier is the driver id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the location cannot be
     * found (404). Or when the location is referenced by one of the driver's
     * stop (409). Or when there is a mismatch between the URI identifier and
     * the data identifier (403).
     */
    @DeleteMapping(path = DRIVER_RESOURCE_PATH)
    public ResponseEntity delete(
            Authentication user,
            @PathVariable("identifier") String identifier
    ) {
        try {
            if (!identifier.equals(user.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            driverService.deleteById(identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private static boolean isOwner(String authenticated, DriverDto submitted) {
        return authenticated != null
                && authenticated.equals(submitted.getIdentifier());
    }

}