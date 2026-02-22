package medical.app.backend.appointment.dto;

import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        String patientUserId,
        String doctorId,
        LocalDateTime scheduledAt,
        AppointmentStatus status,
        String notes,
        LocalDateTime createdAt) {

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientUserId(),
                appointment.getDoctorId(),
                appointment.getScheduledAt(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCreatedAt()
        );
    }
}
