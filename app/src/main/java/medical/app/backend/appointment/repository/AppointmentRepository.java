package medical.app.backend.appointment.repository;

import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUserId(String patientUserId);

    List<Appointment> findByPatientUserIdAndStatus(String patientUserId, AppointmentStatus status);

    List<Appointment> findByScheduledAtBetweenAndStatusNot(
            LocalDateTime from, LocalDateTime to, AppointmentStatus excludedStatus);
}
