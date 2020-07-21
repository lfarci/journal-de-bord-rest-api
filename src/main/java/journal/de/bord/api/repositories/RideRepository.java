package journal.de.bord.api.repositories;

import journal.de.bord.api.entities.Ride;
import org.springframework.data.repository.CrudRepository;

public interface RideRepository extends CrudRepository<Ride, Long> {
}