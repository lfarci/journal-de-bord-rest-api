package journal.de.bord.api.services;

import journal.de.bord.api.entities.Location;
import journal.de.bord.api.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationDatabaseTable implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Location findById(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(
                () -> new IllegalArgumentException(locationId + " is not a location id.")
        );
    }

    @Override
    public Boolean existsById(Long locationId) {
        return locationRepository.existsById(locationId);
    }
}
