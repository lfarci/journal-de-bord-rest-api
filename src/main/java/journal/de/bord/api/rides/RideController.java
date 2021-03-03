package journal.de.bord.api.rides;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.drivers.DriverService;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.stops.Stop;
import journal.de.bord.api.stops.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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
    private RideService rideService;

    @Autowired
    private StopService stopService;

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
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            if (data.getDeparture() == data.getArrival()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Driver driver = driverService.findById(driverId);
            if (driver.ownsRideStops(data)) {
                Long id = rideService.save(driver, stopService.makeRide(data));
                return new ResponseEntity(new Object() {
                    public final Long rideId = id;
                }, HttpStatus.CREATED);
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
     * Gets all the rides for the specified driver.
     *
     * @param driverId is the id of the driver to get the rides for.
     * @return the response containing a list of rides.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = "/rides/{identifier}")
    public ResponseEntity ride(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            Ride ride = rideService.findRideFor(driver, identifier);
            return ResponseEntity.ok(ride);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
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
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            return ResponseEntity.ok(rideService.findAllRidesFor(driver));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            if (data.getDeparture() == data.getArrival()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Driver driver = driverService.findById(driverId);
            if (driver.ownsRideStops(data)) {
                Ride ride = rideService.findRideFor(driver, identifier);
                ride = stopService.updateRide(ride, data);
                rideService.save(driver, ride);
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
     * Deletes the specified ride.
     *
     * @param driverId is the pseudonym of the driver.
     * @param identifier is the ride id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the ride cannot be
     * found (404).
     */
    @DeleteMapping(path = "/rides/{identifier}")
    public ResponseEntity delete(
        Authentication user,
        @PathVariable("driverId") String driverId,
        @PathVariable("identifier") String identifier
    ) {
        try {
            requireAuthenticatedOwner(user.getName(), driverId);
            Driver driver = driverService.findById(driverId);
            rideService.deleteRideFor(driver, identifier);
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
