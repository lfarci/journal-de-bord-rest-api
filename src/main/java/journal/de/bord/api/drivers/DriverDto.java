package journal.de.bord.api.drivers;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class DriverDto {

    /**
     * The identifier is nullable because the create endpoint does not require
     * it. However, when specified the identifier should not be blank.
     */
    @Pattern(regexp="^(?!\\s*$).+")
    private String identifier;

    @PositiveOrZero
    @NotNull
    private Long objective;

    public boolean hasIdentifier() { return identifier != null; }

}
