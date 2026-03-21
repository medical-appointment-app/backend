package medical.app.backend.common.model.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The single doctor operating in this system.
 * Stored in the `doctors` table.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends BasePerson {

    /**
     * Average consultation duration in minutes.
     */
    @Column
    private Integer slotDurationMinutes;
}
