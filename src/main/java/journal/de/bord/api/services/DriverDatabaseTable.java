package journal.de.bord.api.services;

import journal.de.bord.api.entities.Driver;
import journal.de.bord.api.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverDatabaseTable implements DriverService {

    @Autowired
    DriverRepository driverRepository;

    @Override
    public Boolean exist(String pseudonym) {
        return driverRepository.existsById(pseudonym);
    }

    @Override
    public Driver findByPseudonym(String pseudonym) {
        Optional<Driver> driver = driverRepository.findById(pseudonym);
        return driver.orElseThrow(() -> new IllegalArgumentException());
    }

}
