package journal.de.bord.api.drivers;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DriverDto {

    @NotBlank
    @NotNull
    private String identifier;

    @Min(0)
    @NotNull
    private Long objective;

}
