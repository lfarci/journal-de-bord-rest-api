package journal.de.bord.api.drivers;

import java.util.List;

public interface DriverService {
    
    /**
     * Tells if the given identifier is the one of a driver.
     *
     * @param identifier is a potential driver identifier.
     * @return true if the identifier exists.
     * @throws NullPointerException when the identifier argument is null.
     */
    Boolean exist(String identifier);

    /**
     * Finds the driver for the given identifier.
     *
     * @param identifier is the identifier of the driver to get.
     * @return the driver with the given identifier.
     * @throws NullPointerException when the identifier argument is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist.
     */
    Driver findById(String identifier);

    /**
     * Finds all the drivers.
     *
     * @return the list of drivers.
     */
    Iterable<Driver> findAll();

    /**
     * Creates a new driver.
     *
     * @param driver contains the data of the new driver.
     * @throws NullPointerException when the driver argument is null.
     * @throws IllegalStateException when the driver's identifier is
     * already used.
     */
    void create(DriverDto driver);

    /**
     * Updates the specified driver.
     * 
     * @param driver is the driver to update.
     * @throws NullPointerException when the identifier argument is null.
     */
    void update(DriverDto driver);

    /**
     * Deletes the specified driver.
     *
     * @param identifier is the id of the driver to delete.
     * @throws NullPointerException when the identifier argument is null.
     */
    void deleteById(String identifier);

}
