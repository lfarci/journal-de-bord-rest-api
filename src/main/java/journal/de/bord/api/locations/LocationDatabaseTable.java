package journal.de.bord.api.locations;

import journal.de.bord.api.drivers.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LocationDatabaseTable implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Location findById(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(
                () -> new IllegalArgumentException(locationId + " is not a location id.")
        );
    }

    @Override
    public Location findLocationFor(Driver driver, String identifier) {
        try {
            Location result = driver.getLocationById(Long.parseLong(identifier));
            if (result == null) {
                throw new IllegalArgumentException("No location with id: " + identifier);
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("The id is invalid: " + identifier);
        }
    }

    @Override
    public List<Location> findAllLocationsFor(Driver driver) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        return driver.getLocations();
    }

    @Override
    public Boolean existsById(Long locationId) {
        return locationRepository.existsById(locationId);
    }

    @Override
    public Boolean existsByName(String name) {
        return locationRepository.existsByName(name);
    }

    @Override
    public void createNewLocationFor(Driver driver, LocationDto data) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(data, "\"data\" argument is null");
        if (existsByName(data.getName())) {
            throw new IllegalStateException();
        }
        Location location = Location.from(data);
        location.setDriver(driver);
        locationRepository.save(location);
    }

    @Override
    public void updateLocationFor(Driver driver, String identifier, LocationDto data) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        Objects.requireNonNull(data, "\"data\" argument is null");
        try {
            Location location = findLocationFor(driver, identifier);
            location.setValues(data);
            locationRepository.save(location);
        } catch (DataIntegrityViolationException e) {
            String message = String.format(
                "A location with the name \"%s\" already exist.",
                data.getName()
            );
            throw new IllegalStateException(message);
        }

    }

    @Override
    public void deleteLocationFor(Driver driver, String identifier) {
        Objects.requireNonNull(driver, "\"driver\" argument is null");
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        try {
            Location location = findLocationFor(driver, identifier);
            locationRepository.delete(location);
        } catch (NonTransientDataAccessException e) {
            String message = String.format(
                "Location with id %s cannot be deleted: %s",
                identifier, e.getMessage()
            );
            throw new IllegalStateException(message);
        }

    }
}
