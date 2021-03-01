package journal.de.bord.api.rides;

import journal.de.bord.api.stops.StopDto;
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
import java.util.Optional;

/**
 * The controller handle the REST interface exposing the rides resources.
 */
@RestController
@RequestMapping("/api/drivers/{driverId}")
public class RideController {

    private static final String RIDES_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides";
    private static final String RIDE_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides/{identifier}";

    @Autowired
    private DriverService driverService;

    @Autowired
    private RideDatabaseTable rideDatabaseTable;

    /**
     * Creates a new ride.
     *
     * @param driverId is the id of the driver to create a new drive for.
     * @param data is the data describing the ride to create.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the driver does not exist (404) or
     * when the driving is currently driving (422).
     */
    @PostMapping(path = "/rides")
    public ResponseEntity create(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @Valid @RequestBody RideDto data
    ) {
        return ResponseEntity.ok(String.format(
            "Create ride for driver with id %s (user: %s).",
            driverId,
            user.getName()
        ));
    }

    /**
     * Gets all the rides for the specified driver.
     *
     * @param driverId is the id of the driver to get the rides for.
     * @return the response containing a list of rides.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = "/rides/{identifier}")
    public ResponseEntity rides(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @PathVariable("identifier") String identifier
    ) {
        return ResponseEntity.ok(String.format(
            "Get ride with id %s for driver with id %s (user: %s).",
            identifier,
            driverId,
            user.getName()
        ));
    }

    /**
     * Gets all the rides done for the specified driver.
     *
     * @param driverId is the id of the driver to get the rides for.
     * @return the response containing a list of rides.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = "/rides")
    public ResponseEntity rides(
        Authentication user,
        @PathVariable("driverId") String driverId
    ) {
        return ResponseEntity.ok(String.format(
            "Get all rides for driver with id %s (user: %s).",
            driverId,
            user.getName()
        ));
    }

    /**
     * Replaces the specified ride with the given one.
     *
     * @param driverId is the id of the driver to edit a ride for.
     * @param identifier is the id of the ride to edit.
     * @param data is the data of the new ride.
     * @return the response without content (204).
     * @throws ResponseStatusException 404 the specified driver id or ride id
     * cannot be found.
     */
    @PutMapping(path = "/rides/{identifier}")
    public ResponseEntity update(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @PathVariable("identifier") String identifier,
        @Valid @RequestBody RideDto data
    ) {
        return ResponseEntity.ok(String.format(
            "Update ride with id %s for driver with id %s (user: %s).",
            identifier,
            driverId,
            user.getName()
        ));
    }

    /**
     * Deletes the specified ride.
     *
     * @param driverId is the pseudonym of the driver.
     * @param identifier is the ride id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the ride cannot be found (404).
     */
    @DeleteMapping(path = "/rides/{identifier}")
    public ResponseEntity delete(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @PathVariable("identifier") String identifier
    ) {
        return ResponseEntity.ok(String.format(
            "Delete ride with id %s for driver with id %s (user: %s).",
            identifier,
            driverId,
            user.getName()
        ));
    }

}
