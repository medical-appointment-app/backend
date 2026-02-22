package medical.app.backend.appointment.repository;

import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUserId(String patientUserId);

    List<Appointment> findByPatientUserIdAndStatus(String patientUserId, AppointmentStatus status);
}
