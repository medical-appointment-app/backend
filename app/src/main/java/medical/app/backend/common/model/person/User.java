package medical.app.backend.common.model.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A registered patient. Stored in the `users` table.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BasePerson {

    /** The user ID issued by the external auth gateway — links this profile to a session. */
    @Column(unique = true)
    private String externalId;

    @Column
    private LocalDate dateOfBirth;

    @Column(length = 500)
    private String address;

    /**
     * e.g. A+, B-, O+, AB+
     */
    @Column(length = 5)
    private String bloodType;
}
