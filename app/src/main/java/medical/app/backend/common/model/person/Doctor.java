package medical.app.backend.common.model.person;

import jakarta.persistence.*;

/**
 * The single doctor operating in this system.
 * Stored in the `doctors` table.
 */
@Entity
@Table(name = "doctors")
public class Doctor extends BasePerson {

    /**
     * Average consultation duration in minutes.
     */
    @Column
    private Integer slotDurationMinutes;

    public Integer getSlotDurationMinutes() { return slotDurationMinutes; }
    public void setSlotDurationMinutes(Integer slotDurationMinutes) { this.slotDurationMinutes = slotDurationMinutes; }
}
