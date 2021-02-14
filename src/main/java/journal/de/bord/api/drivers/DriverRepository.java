package journal.de.bord.api.drivers;

import journal.de.bord.api.drivers.Driver;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {
}
