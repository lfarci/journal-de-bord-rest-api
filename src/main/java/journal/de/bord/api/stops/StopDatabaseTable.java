package journal.de.bord.api.stops;

import journal.de.bord.api.drivers.Driver;
import journal.de.bord.api.locations.Location;
import journal.de.bord.api.locations.LocationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopDatabaseTable implements StopService {

    @Override
    public Location findById(Long stopId) {
        return null;
    }

    @Override
    public Location findStopFor(Driver driver, String identifier) {
        return null;
    }

    @Override
    public List<Location> findAllStopsFor(Driver driver) {
        return null;
    }

    @Override
    public Boolean existsById(Long locationId) {
        return null;
    }

    @Override
    public void createNewStopFor(Driver driver, LocationDto location) {

    }

    @Override
    public void updateStopFor(Driver driver, String identifier, LocationDto data) {

    }

    @Override
    public void deleteStopFor(Driver driver, String identifier) {

    }
}
