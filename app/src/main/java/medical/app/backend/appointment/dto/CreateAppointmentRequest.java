package medical.app.backend.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotBlank(message = "Doctor ID is required")
        String doctorId,

        @NotNull(message = "Scheduled time is required")
        @Future(message = "Appointment must be scheduled in the future")
        LocalDateTime scheduledAt,

        String notes) {
}
