package farci.logan.jdb;

import farci.logan.jdb.entities.Driver;
import farci.logan.jdb.entities.Ride;
import farci.logan.jdb.entities.Stop;
import farci.logan.jdb.repositories.DriverRepository;
import farci.logan.jdb.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class RidesRestController {

    private static final String RIDES_RESOURCE_PATH = "/api/drivers/{pseudonym}/rides";

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    /**
     * Starts a new ride for the specified driver.
     *
     * @param pseudonym is the pseudonym of the driver to create a new drive for.
     * @param departure is the data describing the departure of the new ride and its driver.
     * @return the response.
     * @throws ResponseStatusException when the driver does not exist (404) or when the driving is currently driving
     * (422).
     */
    @PostMapping(path = RIDES_RESOURCE_PATH)
    public ResponseEntity start(@PathVariable("pseudonym") String pseudonym, @Valid @RequestBody Stop departure) {
        Optional<Driver> driver = driverRepository.findById(pseudonym);
        if (driver.isPresent()) {
            if (driver.get().isDriving()) {
                String errorMessage = "The driver " + pseudonym + " is driving and cannot start a new ride.";
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
            }
            rideRepository.save(new Ride(departure, driver.get()));
            return ResponseEntity.ok("Started a new drive for: " + driver);
        } else {
            String errorMessage = "No driver with the pseudonym " + pseudonym;
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
    }

    @GetMapping(path = RIDES_RESOURCE_PATH)
    public String rides() {
        return "Rides end point";
    }

    @PatchMapping(path = RIDES_RESOURCE_PATH)
    public String update() {
        return "Update end point";
    }

}
