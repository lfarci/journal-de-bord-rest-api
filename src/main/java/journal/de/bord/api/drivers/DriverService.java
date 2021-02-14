package journal.de.bord.api.drivers;

import journal.de.bord.api.drivers.Driver;

public interface DriverService {

    /**
     * Tells if the given pseudonym is the one of a driver.
     *
     * @param pseudonym is a potential pseudonym.
     * @return true if the pseudonym exists.
     */
    Boolean exist(String pseudonym);

    /**
     * Finds the driver for the given pseudonym.
     *
     * @param pseudonym is the pseudonym of the driver to get.
     * @return the driver with the given pseudonym.
     * @throws NullPointerException when the pseudonym argument is null.
     * @throws IllegalArgumentException when the specified driver does not
     * exist.
     */
    Driver findByPseudonym(String pseudonym);

}
