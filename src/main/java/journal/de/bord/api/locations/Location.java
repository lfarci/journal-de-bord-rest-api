package journal.de.bord.api.locations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import journal.de.bord.api.drivers.Driver;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Represents one of the location visited by a driver. A driver visits a location by stopping at it.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Data
@NoArgsConstructor
public class Location {

    public static Location from(LocationDto data) {
        return new Location(data.getName(), data.getLatitude(), data.getLongitude());
    }

    /**
     * Identifies this entity.
     */
    @Id
    @GeneratedValue(generator = "location_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "location_sequence_generator",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    private Long id;

    /**
     * Is a name used to identify this location with a term familiar to a driver.
     */
    @NotBlank
    @NotNull
    private String name;

    /**
     * Is the latitude of the location.
     */
    @NotNull
    private Double latitude;

    /**
     * Is the longitude of the location.
     */
    @NotNull()
    private Double longitude;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Driver driver;

    public Location(@NotBlank @NotNull String name, @NotNull Double latitude, @NotNull Double longitude) {
        this.id = -1L;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setValues(LocationDto data) {
        setName(data.getName());
        setLatitude(data.getLatitude());
        setLongitude(data.getLongitude());
    }

}