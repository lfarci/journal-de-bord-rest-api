package journal.de.bord.api.services;

import journal.de.bord.api.dto.StopDto;
import journal.de.bord.api.entities.Driver;
import journal.de.bord.api.entities.Location;
import journal.de.bord.api.entities.Ride;
import journal.de.bord.api.entities.Stop;
import journal.de.bord.api.repositories.DriverRepository;
import journal.de.bord.api.repositories.LocationRepository;
import journal.de.bord.api.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RideDatabaseTable implements RideService {

    @Autowired
    RideRepository rideRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    LocationRepository locationRepository;

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

    private Stop fromDto(StopDto stopDto) {
        Optional<Location> location = locationRepository.findById(stopDto.getLocationId());
        if (location.isPresent()) {
            return new Stop(stopDto.getMoment(), stopDto.getOdometerValue(), location.get());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void start(Driver driver, StopDto departure) {
        Objects.requireNonNull(driver, "\"driver\" argument is null.");
        Objects.requireNonNull(driver, "\"stop\" argument is null.");
        Stop stop = fromDto(departure);
        if (!driver.canStartWith(stop)) {
            throw new IllegalStateException(driver.getPseudonym() + " cannot start a ride.");
        }
        if (!driverRepository.existsById(driver.getPseudonym())) {
            throw new IllegalArgumentException(driver.getPseudonym() + " doesn't exist.");
        }
        if (!locationRepository.existsById(stop.getLocation().getId())) {
            throw new IllegalArgumentException("The departure location doesn't exist.");
        }
        rideRepository.save(new Ride(stop, driver));
    }

    @Override
    public void update(Ride updatedRide) {
        Objects.requireNonNull(updatedRide, "\"updatedRide\" argument is null.");
        Ride ride = rideRepository.findById(updatedRide.getId()).orElseThrow(() -> new IllegalArgumentException());
        ride.setDeparture(updatedRide.getDeparture());
        ride.setArrival(updatedRide.getArrival());
        ride.setTrafficCondition(updatedRide.getTrafficCondition());
        ride.setComment(updatedRide.getComment());
        rideRepository.save(ride);
    }

    @Override
    public void deleteById(Long rideId) {
        Objects.requireNonNull(rideId, "\"rideId\" argument is null.");
        rideRepository.deleteById(rideId);
    }
}
