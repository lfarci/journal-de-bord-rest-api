package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.stops.Stop;
import journal.de.bord.api.stops.StopDto;
import journal.de.bord.api.stops.StopRepository;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;

@Service
public class StopDatabaseTable implements StopService {


    @Autowired
    StopRepository stopRepository;

    @Override
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

    @Override
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

    @Override
    public void updateStopFor(Driver driver, String identifier, StopDto data) {

    }

    @Override
    public void deleteStopFor(Driver driver, String identifier) {

    }
}
