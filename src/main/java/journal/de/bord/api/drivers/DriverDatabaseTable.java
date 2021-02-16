package journal.de.bord.api.drivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DriverDatabaseTable implements DriverService {

    @Autowired
    DriverRepository driverRepository;

    @Override
    public Boolean exist(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        return driverRepository.existsById(identifier);
    }

    @Override
    public Driver findById(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        Optional<Driver> driver = driverRepository.findById(identifier);
        return driver.orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Iterable<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    public void create(DriverDto data) {
        Objects.requireNonNull(data, "\"data\" argument is null");
        try {
            Driver driver = new Driver(data.getIdentifier(), data.getObjective());
            driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("Driver with id %s already exist.", data.getIdentifier());
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void update(DriverDto data) {
        Objects.requireNonNull(data, "\"data\" argument is null");
        try {
            Driver driver = findById(data.getIdentifier());
            driver.setObjective(data.getObjective());
            driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("Driver with id %s already exist.", data.getIdentifier());
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void deleteById(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        if (!exist(identifier)) {
            String msg = String.format("Driver with id %s does not exist.", identifier);
            throw new IllegalArgumentException(msg);
        }
        driverRepository.deleteById(identifier);
    }
}
