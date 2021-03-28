package journal.de.bord.api.drivers;

import journal.de.bord.api.drivers.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {

    @Query("SELECT COUNT(r) FROM Ride r WHERE r.driver.identifier = ?1")
    Long countDriverRides(String identifier);

    @Query(
        value = "SELECT SUM(a.odometer_value - d.odometer_value)\n" +
                "FROM RIDE r\n" +
                "     JOIN STOP d ON d.id = r.departure_id\n" +
                "     JOIN STOP a ON a.id = r.arrival_id\n" +
                "WHERE r.driver_identifier like ?1",
        nativeQuery = true
    )
    Long sumDriverRidesDistances(String identifier);

}
