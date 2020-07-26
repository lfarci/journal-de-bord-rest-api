package journal.de.bord.api.controllers;

import journal.de.bord.api.dto.RideDto;
import journal.de.bord.api.dto.StopDto;
import journal.de.bord.api.entities.Driver;
import journal.de.bord.api.entities.Ride;
import journal.de.bord.api.repositories.DriverRepository;
import journal.de.bord.api.services.DriverDatabaseTable;
import journal.de.bord.api.services.RideDatabaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * The controller handle the REST interface exposing the rides resources.
 */
@RestController
public class RidesRestController {

    private static final String RIDES_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides";
    private static final String RIDE_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides/{identifier}";

    @Autowired
    private DriverDatabaseTable driverDatabaseTable;

    @Autowired
    private RideDatabaseTable rideDatabaseTable;

    /**
     * Starts a new ride for the specified driver with the given request body.
     * 201 status code is returned on success.
     *
     * The request body content is of JSON type and has the following fields:
     * - "moment": a string representing a date and time (ISO 8601), e.g. "2020-01-01T14:20:01".
     * - locationId: a string representing the location id of the stop.
     * - "odometerValue": a string representing a long integer.
     *
     * @param pseudonym is the pseudonym of the driver to create a new drive for.
     * @param departure is the data describing the departure of the new ride and its driver.
     * @return the response without content (created status, 201).
     * @throws ResponseStatusException when the driver does not exist (404) or when the driving is currently driving
     * (422).
     */
    @PostMapping(path = RIDES_RESOURCE_PATH)
    public ResponseEntity start(
            @PathVariable("pseudonym") String pseudonym,
            @Valid @RequestBody StopDto departure
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            rideDatabaseTable.start(driver, departure);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets the specified driver list of rides.
     *
     * @param pseudonym is the pseudonym of the driver to get the rides for.
     * @param last if specified and true then the endpoint gets the last ride for the specified driver.
     * @return the response containing a list of rides.
     * @throws ResponseStatusException 404 the specified driver does not exist.
     */
    @GetMapping(path = RIDES_RESOURCE_PATH)
    public ResponseEntity rides(
            @PathVariable("pseudonym") String pseudonym,
            @RequestParam("last") Optional<Boolean> last
    ) {
        try {
            Driver driver = driverDatabaseTable.findByPseudonym(pseudonym);
            List<Ride> rides = rideDatabaseTable.getAllDriverRides(driver, last);
            return ResponseEntity.ok(rides);
        } catch (NullPointerException | IllegalArgumentException exception) {
            String errorMessage = "No driver with the pseudonym " + pseudonym;
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }

    /**
     * Replaces the specified ride with the given one.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the ride id.
     * @param data is the data of the new ride.
     * @return the response without content (204).
     * @throws ResponseStatusException if the identifier or the data is not val.
     */
    @PutMapping(path = RIDE_RESOURCE_PATH)
    public ResponseEntity update(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier,
            @Valid @RequestBody RideDto data
    ) {
        // TODO: the driver pseudonym is useless!
        try {
            rideDatabaseTable.update(identifier, data);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException((HttpStatus.UNPROCESSABLE_ENTITY));
        }
    }

    /**
     * Deletes the specified ride.
     *
     * @param pseudonym is the pseudonym of the driver.
     * @param identifier is the ride id.
     * @return the response without content (204).
     * @throws ResponseStatusException if the driver or the ride cannot be found (404).
     */
    @DeleteMapping(path = RIDE_RESOURCE_PATH)
    public ResponseEntity delete(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier
    ) {
        // TODO: the driver pseudonym is useless!
        try {
            rideDatabaseTable.deleteById(identifier);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
