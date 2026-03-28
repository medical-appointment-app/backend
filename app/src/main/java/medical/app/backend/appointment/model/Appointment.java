package medical.app.backend.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.app.backend.appointment.enums.AppointmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Optimistic lock — prevents concurrent modifications to the same record. */
    @Version
    private Long version;

    @Column(nullable = false)
    private String patientUserId;

    @Column(nullable = false)
    private String doctorId;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(length = 1000)
    private String notes;

    @Setter(lombok.AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Duration of the appointment in minutes. Nullable — filled from doctor's slot duration on create. */
    @Column
    private Integer durationMinutes;

    /** Non-null only when status = LOCKED. After this instant the slot is released. */
    @Column
    private LocalDateTime lockedUntil;

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = AppointmentStatus.PENDING;
        }
    }
}
