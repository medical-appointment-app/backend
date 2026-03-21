package medical.app.backend.common.model.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A back-office administrator. Stored in the `admins` table.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends BasePerson {

    /**
     * Coarse-grained access level.
     * SUPER_ADMIN  – full system access
     * CONTENT_ADMIN – manages information pages and catalog
     * SUPPORT      – read-only access for patient support
     */
    public enum AccessLevel {
        SUPER_ADMIN,
        CONTENT_ADMIN,
        SUPPORT
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel accessLevel;
}
