package journal.de.bord.api.controllers;

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
import java.util.Arrays;
import java.util.Optional;

@RestController
public class RidesRestController {

    private static final String RIDES_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides";
    private static final String RIDE_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides/{identifier}";

    @Autowired
    private DriverRepository driverRepository;

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
     * @return the response.
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
     * @throws ResponseStatusException when the specified driver does not exist.
     */
    @GetMapping(path = RIDES_RESOURCE_PATH)
    public ResponseEntity rides(
            @PathVariable("pseudonym") String pseudonym,
            @RequestParam("last") Optional<Boolean> last
    ) {
        Optional<Driver> driver = driverRepository.findById(pseudonym);
        if (driver.isPresent()) {
            if (driver.get().hasDriven() && last.isPresent() && last.get()) {
                return ResponseEntity.ok(Arrays.asList(driver.get().getLastRide().get()));
            } else {
                return ResponseEntity.ok(driver.get().getRides());
            }
        } else {
            String errorMessage = "No driver with the pseudonym " + pseudonym;
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }

    @PatchMapping(path = RIDE_RESOURCE_PATH)
    public ResponseEntity update(
            @PathVariable("pseudonym") String pseudonym,
            @PathVariable("identifier") String identifier,
            @RequestBody Ride updatedRide
    ) {
//        Optional<Ride> ride = rideRepository.findById(updatedRide.getId());
//        // TODO: is the provided ride valid? The departure should be before the arrival
//        if (ride.isPresent()) {
//            // TODO: check the driver pseudonym and throw a 404 if it does not match
//            Ride newRide = ride.get();
//            newRide.setArrival(updatedRide.getArrival());
//            newRide.setTrafficCondition(updatedRide.getTrafficCondition());
//            newRide.setComment(updatedRide.getComment());
//            System.out.println("updated ride is...\n" + newRide.getDriver().getPseudonym());
////            rideRepository.save(newRide);
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        } else {
//            String errorMessage = "No ride with the specified identifier " + identifier;
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
//        }
        return ResponseEntity.ok("update");
    }

    @DeleteMapping(path = RIDE_RESOURCE_PATH)
    public String delete(@PathVariable("pseudonym") String pseudonym, @Valid @RequestBody Ride ride) {
//        rideRepository.save(ride);
        return "Update end point";
    }

}
