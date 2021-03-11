package journal.de.bord.api.drivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    DriverRepository driverRepository;

    /**
     * Tells if the given identifier is the one of a driver.
     *
     * @param identifier is a potential driver identifier.
     * @return true if the identifier exists.
     * @throws NullPointerException when the identifier argument is null.
     */
    public Boolean exist(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        return driverRepository.existsById(identifier);
    }

    /**
     * Finds the driver for the given identifier.
     *
     * @param identifier is the identifier of the driver to get.
     * @return the driver with the given identifier.
     * @throws NullPointerException when the identifier argument is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist.
     */
    public Driver findById(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        Optional<Driver> driver = driverRepository.findById(identifier);
        return driver.orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Finds all the drivers.
     *
     * @return the list of drivers.
     */
    public Iterable<Driver> findAll() {
        return driverRepository.findAll();
    }

    /**
     * Creates a new driver.
     *
     * @param data contains the data of the new driver.
     * @throws NullPointerException when the driver argument is null.
     * @throws IllegalStateException when the driver's identifier is
     * already used.
     */
    public void create(DriverDto data) {
        Objects.requireNonNull(data, "\"data\" argument is null");
        if (driverRepository.existsById(data.getIdentifier())) {
            throw new IllegalStateException(String.format(
                "Driver with id %s already exist.",
                data.getIdentifier()
            ));
        } else {
            Driver driver = new Driver(data.getIdentifier(), data.getObjective());
            driverRepository.save(driver);
        }
    }

    /**
     * Updates the specified driver.
     *
     * @param data is the driver to update.
     * @throws NullPointerException when the identifier argument is null.
     */
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

    /**
     * Deletes the specified driver.
     *
     * @param identifier is the id of the driver to delete.
     * @throws NullPointerException when the identifier argument is null.
     */
    public void deleteById(String identifier) {
        Objects.requireNonNull(identifier, "\"identifier\" argument is null");
        if (!exist(identifier)) {
            String msg = String.format("Driver with id %s does not exist.", identifier);
            throw new IllegalArgumentException(msg);
        }
        driverRepository.deleteById(identifier);
    }
}
