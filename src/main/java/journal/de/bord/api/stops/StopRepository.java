package journal.de.bord.api.stops;

import journal.de.bord.api.locations.Location;
import org.springframework.data.repository.CrudRepository;

public interface StopRepository extends CrudRepository<Stop, Long> {

}