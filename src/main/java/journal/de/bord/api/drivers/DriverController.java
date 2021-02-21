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
     * Creates a new driver.
     *
     * @param driver is the pseudonym of the driver to create a new location
     * for.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the driver identifier already
     * exists (409). Or when the provided data results in a null pointer
     * exception (422).
     */
    @PostMapping(path = DRIVERS_RESOURCE_PATH)
    public ResponseEntity create(@Valid @RequestBody DriverDto driver) {
        try {
            driverService.create(driver);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
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
            Authentication authentication,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody DriverDto data
    ) {
        try {
            String userId = authentication.getName();
            if (!userId.equals(identifier)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted to the owner.");
            }
            if (!identifier.equals(data.getIdentifier())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
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
            Authentication authentication,
            @PathVariable("identifier") String identifier
    ) {
        try {
            String userId = authentication.getName();
            if (!userId.equals(identifier)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted to the owner.");
            }
            driverService.deleteById(identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}