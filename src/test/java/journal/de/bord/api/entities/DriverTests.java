package journal.de.bord.api.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DriverTests {

    private static LocalDateTime toLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, formatter);
    }

    @Test
    public void driverCanStartWithoutPastRides() {
        List<Ride> rides = new ArrayList<>();
        Driver driver = new Driver("pseudonym", 1000L, rides, new ArrayList<>());
        assertThat(driver.canStartWith(new Stop())).isTrue();
    }

    @Test
    public void driverCanStartWithStopAfterLastRideArrival() {
        LocalDateTime departureMoment = toLocalDateTime("2020-01-01 13:30");
        LocalDateTime arrivalMoment = toLocalDateTime("2020-01-01 14:30");
        LocalDateTime nextMoment = toLocalDateTime("2020-01-01 14:45");
        Stop departure = new Stop(departureMoment, 10000L, new Location());
        Stop arrival = new Stop(arrivalMoment, 10050L, new Location());
        Stop nextStop = new Stop(nextMoment, 10100L, new Location());
        Ride ride = new Ride(departure, arrival);
        List<Ride> rides = new ArrayList<>(Arrays.asList(ride));
        Driver driver = new Driver("pseudonym", 1000L, rides, new ArrayList<>());
        assertThat(driver.canStartWith(nextStop)).isTrue();
    }

    @Test
    public void driverCannotStartWhenDriving() {
        Stop departure = new Stop(LocalDateTime.now(), 10000L, new Location());
        Ride ride = new Ride(departure);
        List<Ride> rides = new ArrayList<>(Arrays.asList(ride));
        Driver driver = new Driver("pseudonym", 1000L, rides, new ArrayList<>());
        assertThat(driver.canStartWith(departure)).isFalse();
    }

    @Test
    public void driverCannotStartWithANullStop() {
        List<Ride> rides = new ArrayList<>();
        Driver driver = new Driver("pseudonym", 1000L, rides, new ArrayList<>());
        assertThat(driver.canStartWith(null)).isFalse();
    }

    @Test
    public void driverCannotStartWithStopBeforeLastRideArrival() {
        LocalDateTime departureMoment = toLocalDateTime("2020-01-01 13:30");
        LocalDateTime faultyMoment = toLocalDateTime("2020-01-01 14:00");
        LocalDateTime arrivalMoment = toLocalDateTime("2020-01-01 14:30");
        Stop departure = new Stop(departureMoment, 10000L, new Location());
        Stop faultyStop = new Stop(faultyMoment, 10050L, new Location());
        Stop arrival = new Stop(arrivalMoment, 10100L, new Location());
        Ride ride = new Ride(departure, arrival);
        List<Ride> rides = new ArrayList<>(Arrays.asList(ride));
        Driver driver = new Driver("pseudonym", 1000L, rides, new ArrayList<>());
        assertThat(driver.canStartWith(faultyStop)).isFalse();
    }


}
