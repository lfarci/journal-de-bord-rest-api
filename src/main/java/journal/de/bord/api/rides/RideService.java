package journal.de.bord.api.rides;

import journal.de.bord.api.locations.LocationService;
import journal.de.bord.api.stops.StopDto;
import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.stops.Stop;
import journal.de.bord.api.drivers.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    /**
     * Finds one of the specified driver's ride by id.
     *
     * @param driver is the driver to get a ride for.
     * @param identifier is the identifier of the ride to find.
     * @return the ride with the specified id.
     * @throws IllegalArgumentException if the given id doesn't exist or is not
     * a parsable long.
     * @throws NullPointerException if the id is null.
     */
    public Ride findRideFor(Driver driver, String identifier) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        try {
            Ride result = driver.getRideById(Long.parseLong(identifier));
            if (result == null) {
                throw new IllegalArgumentException("No ride with id: " + identifier);
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("The id is invalid: " + identifier);
        }
    }

    /**
     * Gets the list of rides for the specified driver.
     *
     * @param driver is the driver to get rides for.
     * @return the response containing a list of rides.
     * @throws IllegalArgumentException when the specified driver does not
     * exist.
     * @throws NullPointerException when the driver is null.
     */
    public List<Ride> findAllRidesFor(Driver driver) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        return driver.getRides();
    }

    public Long save(Driver driver, Ride ride) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(ride, "\"ride\" argument is null");
        try {
            ride.setDriver(driver);
            rideRepository.save(ride);
            return ride.getId();
        } catch (NonTransientDataAccessException e) {
            throw new IllegalStateException();
        }
    }

    public void start(Driver driver, StopDto departure) {
        Objects.requireNonNull(driver, "\"driver\" argument is null.");
        Objects.requireNonNull(driver, "\"stop\" argument is null.");
    }

    public void update(String identifier, RideDto data) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null.");
        Objects.requireNonNull(data, "\"data\" argument is null.");
    }

    public void deleteById(String rideId) {
        Objects.requireNonNull(rideId, "\"rideId\" argument is null.");
    }

}
