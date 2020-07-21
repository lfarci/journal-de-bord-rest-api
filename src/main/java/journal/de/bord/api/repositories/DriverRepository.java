package journal.de.bord.api.repositories;

import journal.de.bord.api.entities.Driver;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {
}
