package medical.app.backend.common.model.person;

import jakarta.persistence.*;

/**
 * A back-office administrator. Stored in the `admins` table.
 */
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

    public AccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(AccessLevel accessLevel) { this.accessLevel = accessLevel; }
}
