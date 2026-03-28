package medical.app.backend.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LockSlotRequest(
        @NotNull(message = "Scheduled time is required")
        @Future(message = "Appointment must be in the future")
        LocalDateTime scheduledAt,

        @NotBlank(message = "Doctor ID is required")
        String doctorId,

        String notes) {
}
