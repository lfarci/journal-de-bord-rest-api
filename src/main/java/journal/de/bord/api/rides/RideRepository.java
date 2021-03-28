package journal.de.bord.api.rides;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {

    Page<Ride> findByDriverIdentifier(String driverIdentifier, Pageable pageable);

}