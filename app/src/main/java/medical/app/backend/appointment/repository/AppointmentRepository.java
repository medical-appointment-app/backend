package medical.app.backend.appointment.repository;

import jakarta.persistence.LockModeType;
import medical.app.backend.appointment.enums.AppointmentStatus;
import medical.app.backend.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUserId(String patientUserId);

    List<Appointment> findByPatientUserIdAndStatus(String patientUserId, AppointmentStatus status);

    List<Appointment> findByScheduledAtBetweenAndStatusNot(
            LocalDateTime from, LocalDateTime to, AppointmentStatus excludedStatus);

    /**
     * Finds an active (non-expired) record for the exact slot time.
     * Uses a PESSIMISTIC_WRITE lock so that concurrent transactions queue up here
     * rather than both reading "slot is free" and racing to insert.
     * A record is "active" when:
     *   - status is anything except CANCELLED, AND
     *   - if status is LOCKED, its lockedUntil has not passed yet.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT a FROM Appointment a
            WHERE a.scheduledAt = :scheduledAt
              AND a.status <> :cancelled
              AND (a.status <> :locked OR a.lockedUntil > :now)
            """)
    Optional<Appointment> findActiveSlotForLocking(
            @Param("scheduledAt") LocalDateTime scheduledAt,
            @Param("cancelled") AppointmentStatus cancelled,
            @Param("locked") AppointmentStatus locked,
            @Param("now") LocalDateTime now);

    /**
     * Returns scheduled times of all active (non-expired) appointments in a time window.
     * Used to compute truly free slots: expired locks are treated as available again.
     */
    @Query("""
            SELECT a.scheduledAt FROM Appointment a
            WHERE a.scheduledAt >= :from
              AND a.scheduledAt < :to
              AND a.status <> :cancelled
              AND (a.status <> :locked OR a.lockedUntil > :now)
            """)
    List<LocalDateTime> findActiveScheduledTimesBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("cancelled") AppointmentStatus cancelled,
            @Param("locked") AppointmentStatus locked,
            @Param("now") LocalDateTime now);

    /** Returns all LOCKED appointments whose lock window has already expired. */
    @Query("SELECT a FROM Appointment a WHERE a.status = :locked AND a.lockedUntil < :now")
    List<Appointment> findExpiredLocks(
            @Param("locked") AppointmentStatus locked,
            @Param("now") LocalDateTime now);

    /** Bulk-releases expired locks in one UPDATE to avoid loading every row. */
    @Modifying
    @Query("""
            UPDATE Appointment a SET a.status = :cancelled, a.lockedUntil = null
            WHERE a.status = :locked AND a.lockedUntil < :now
            """)
    int releaseExpiredLocks(
            @Param("locked") AppointmentStatus locked,
            @Param("cancelled") AppointmentStatus cancelled,
            @Param("now") LocalDateTime now);
}
