package journal.de.bord.api.locations;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LocationDto {

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

}
