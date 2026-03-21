package medical.app.backend.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AvailableSlotResponse(
        LocalDate date,
        LocalTime time,
        int durationMinutes) {
}
