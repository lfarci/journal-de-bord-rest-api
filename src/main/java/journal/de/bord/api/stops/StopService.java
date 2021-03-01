package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.rides.Ride;
import journal.de.bord.api.rides.RideDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StopService {

    @Autowired
    StopRepository stopRepository;

    public boolean containsDepartureOf(RideDto ride) {
        return stopRepository.existsById(ride.getDeparture());
    }

    public boolean containsArrivalOf(RideDto ride) {
        return ride == null || stopRepository.existsById(ride.getArrival());
    }

    public Stop findStopFor(Driver driver, String identifier) {
        try {
            Stop result = driver.getStopById(Long.parseLong(identifier));
            if (result == null) {
                throw new IllegalArgumentException("No stop with id: " + identifier);
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("The id is invalid: " + identifier);
        }
    }

    public Stop findStopFor(Driver driver, Long identifier) {
        Stop result = driver.getStopById(identifier);
        if (result == null) {
            throw new IllegalArgumentException("No stop with id: " + identifier);
        }
        return result;
    }

    public List<Stop> findAllStopsFor(Driver driver) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        return driver.getStops();
    }

    public Long createNewStopFor(Driver driver, StopDto data, Location location) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(data, "\"data\" argument is null");
        Objects.requireNonNull(location, "\"location\" argument is null");
        try {
            Stop stop = Stop.from(data, location);
            stop.setDriver(driver);
            Stop saved = stopRepository.save(stop);
            return saved.getId();
        } catch (NonTransientDataAccessException e) {
            throw new IllegalStateException();
        }
    }

    public void updateStopFor(String id, Driver driver, StopDto data, Location location) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(data, "\"data\" argument is null");
        Objects.requireNonNull(location, "\"location\" argument is null");
        try {
            Stop stop = findStopFor(driver, id);
            stop.setValues(data, location);
            stopRepository.save(stop);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException();
        }
    }

    public void deleteStopFor(Driver driver, String identifier) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        try {
            Stop stop = findStopFor(driver, identifier);
            stopRepository.delete(stop);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException();
        }
    }

    public boolean containsRideStops(RideDto ride) {
        return containsDepartureOf(ride) && containsArrivalOf(ride);
    }

    private Stop findRideDeparture(RideDto ride) {
        if (containsDepartureOf(ride)) {
            return stopRepository.findById(ride.getDeparture()).get();
        } else {
            throw new IllegalStateException(String.format(
                "Cannot find departure with id: %d",
                ride.getDeparture())
            );
        }
    }

    private Stop findRideArrival(RideDto ride) {
        if (containsArrivalOf(ride)) {
            return stopRepository.findById(ride.getArrival()).get();
        } else {
            throw new IllegalStateException(String.format(
                "Cannot find arrival with id: %d",
                ride.getArrival())
            );
        }
    }

    public Ride makeRide(RideDto data) {
        try {
            Stop departure = findRideDeparture(data);
            Stop arrival = findRideArrival(data);
            Ride ride = new Ride();
            ride.setDeparture(departure);
            ride.setArrival(arrival);
            ride.setTrafficCondition(data.getTrafficCondition());
            ride.setComment(data.getComment());
            return ride;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                "Could not make the ride: " + e.getMessage()
            );
        }
    }

}
