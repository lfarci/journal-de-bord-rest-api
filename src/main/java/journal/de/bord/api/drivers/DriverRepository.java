package journal.de.bord.api.drivers;

import journal.de.bord.api.drivers.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {

    @Query("SELECT COUNT(r) FROM Ride r WHERE r.driver.identifier = ?1")
    Long countDriverRides(String identifier);

}
