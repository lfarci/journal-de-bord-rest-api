package farci.logan.jdb.repositories;

import farci.logan.jdb.entities.Driver;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DriverRepository extends CrudRepository<Driver, String> {
}
