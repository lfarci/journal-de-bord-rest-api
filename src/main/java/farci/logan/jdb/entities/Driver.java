package farci.logan.jdb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Driver {

    @Id
    private String pseudonym;

    @NotNull
    @Min(0)
    private Long objective;

    @OneToMany(mappedBy = "driver")
    private List<Ride> rides;

}