package farci.logan.jdb.entities;

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

    /**
     * Identifies this entity.
     */
    @Id
    @GeneratedValue
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

    public Location(@NotBlank @NotNull String name, @NotNull Double latitude, @NotNull Double longitude) {
        this.id = 0L;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}