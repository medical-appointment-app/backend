package medical.app.backend.appointment.dto;

import java.time.LocalDate;

public record WeekAppointmentsQuery(LocalDate weekStart) {
}
