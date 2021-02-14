package journal.de.bord.api.rides;

import journal.de.bord.api.stops.StopDto;
import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.stops.Stop;
import journal.de.bord.api.locations.LocationService;
import journal.de.bord.api.drivers.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Service
public class RideDatabaseTable implements RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private LocationService locationService;

    private Validator validator;

    public RideDatabaseTable() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public Ride findById(String identifier) {
        try {
            Objects.requireNonNull(identifier, "\"identifier\" argument is null.");
            return rideRepository
                    .findById(Long.parseLong(identifier))
                    .orElseThrow(() -> new IllegalArgumentException("The ride doesn't exist."));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("The id should be a parsable long: " + identifier);
        }
    }

    @Override
    public List<Ride> getAllDriverRides(Driver driver, Optional<Boolean> last) {
        Objects.requireNonNull(driver, "\"driver\" argument is null.");
        Objects.requireNonNull(driver, "\"last\" argument is null.");
        if (!driver.hasDriven() && last.orElse(false)) {
            throw new IllegalStateException();
        }
        if (driver.hasDriven() && last.orElse(false)) {
            return Arrays.asList(driver.getLastRide().get());
        }
        return driver.getRides();
    }

    @Override
    public void start(Driver driver, StopDto departure) {
        Objects.requireNonNull(driver, "\"driver\" argument is null.");
        Objects.requireNonNull(driver, "\"stop\" argument is null.");
        if (!isValid(departure)) {
            throw new IllegalStateException();
        }
        Stop stop = fromDto(departure);
        if (!driver.canStartWith(stop)) {
            throw new IllegalStateException(driver.getPseudonym() + " cannot start a ride.");
        }
        if (!driverRepository.existsById(driver.getPseudonym())) {
            throw new IllegalArgumentException(driver.getPseudonym() + " doesn't exist.");
        }
        if (!locationService.existsById(stop.getLocation().getId())) {
            throw new IllegalArgumentException("The departure location doesn't exist.");
        }
        rideRepository.save(new Ride(stop, driver));
    }



    @Override
    public void update(String identifier, RideDto data) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null.");
        Objects.requireNonNull(data, "\"data\" argument is null.");
        if (!isValid(data)) {
            throw new IllegalStateException("Trying to update a ride with invalid data");
        }
        Ride ride = findById(identifier);
        if (!ride.isDriver(data.getDriverPseudonym())) {
            throw new IllegalArgumentException("Driver mismatch");
        }
        updateRide(ride, data);
        rideRepository.save(ride);
    }

    @Override
    public void deleteById(String rideId) {
        Objects.requireNonNull(rideId, "\"rideId\" argument is null.");
        Ride ride = findById(rideId);
        rideRepository.delete(ride);
    }

    private Boolean isValid(RideDto data) {
        Set<ConstraintViolation<RideDto>> violations = validator.validate(data);
        return violations.size() == 0;
    }

    private Boolean isValid(StopDto data) {
        Set<ConstraintViolation<StopDto>> violations = validator.validate(data);
        return violations.size() == 0;
    }

    private Stop fromDto(StopDto stopDto) {
        Location location = locationService.findById(stopDto.getLocationId());
        return new Stop(stopDto.getMoment(), stopDto.getOdometerValue(), location);
    }

    private void updateRide(Ride ride, RideDto data) {
        ride.setDeparture(fromDto(data.getDeparture()));
        ride.setArrival(fromDto(data.getArrival()));
        ride.setTrafficCondition(data.getTrafficCondition());
        ride.setComment(data.getComment());
    }

}
