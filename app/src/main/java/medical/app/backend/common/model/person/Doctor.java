package medical.app.backend.common.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private int slotDurationMinutes;
}
