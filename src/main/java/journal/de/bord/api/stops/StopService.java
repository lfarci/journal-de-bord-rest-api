package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
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

    public List<Stop> findAllStopsFor(Driver driver) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        return driver.getStops();
    }

    public void createNewStopFor(Driver driver, StopDto data, Location location) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(data, "\"data\" argument is null");
        Objects.requireNonNull(location, "\"location\" argument is null");
        try {
            Stop stop = Stop.from(data, location);
            stop.setDriver(driver);
            stopRepository.save(stop);
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
}
