package journal.de.bord.api.repositories;

import journal.de.bord.api.entities.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
}