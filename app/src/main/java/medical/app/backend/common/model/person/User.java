package medical.app.backend.common.model.person;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * A registered patient. Stored in the `users` table.
 */
@Entity
@Table(name = "users")
public class User extends BasePerson {

    @Column
    private LocalDate dateOfBirth;

    @Column(length = 500)
    private String address;

    /**
     * e.g. A+, B-, O+, AB+
     */
    @Column(length = 5)
    private String bloodType;

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
}
