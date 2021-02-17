package journal.de.bord.api.locations;

import journal.de.bord.api.locations.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {

    Boolean existsByName(String name);

}