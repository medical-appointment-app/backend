package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AvailableSlotResponse;
import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GetAvailableAppointmentsUseCase {

    private static final LocalTime WORK_START = LocalTime.of(9, 0);
    private static final LocalTime WORK_END   = LocalTime.of(17, 0);

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public GetAvailableAppointmentsUseCase(AppointmentService appointmentService,
                                           DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    // ── Booked appointment queries (what is already taken) ────────────────

    public List<AppointmentResponse> executeBookedForDay(DayAppointmentsQuery query) {
        return appointmentService.getBookedAppointmentsForDay(query);
    }

    public List<AppointmentResponse> executeBookedForWeek(WeekAppointmentsQuery query) {
        return appointmentService.getBookedAppointmentsForWeek(query);
    }

    public List<AppointmentResponse> executeBookedForMonth(MonthAppointmentsQuery query) {
        return appointmentService.getBookedAppointmentsForMonth(query);
    }

    // ── Free slot computation (what is still open) ─────────────────────────

    /**
     * Returns all open time slots for a given day based on:
     * - Doctor's slotDurationMinutes (default 30)
     * - Working hours: 09:00 – 17:00
     * - Existing non-cancelled appointments on that day
     */
    public List<AvailableSlotResponse> executeSlotsForDay(LocalDate date) {
        int slotDuration = doctorService.getSlotDurationMinutes();

        // Use getActiveTimesForDay so that expired locks free up the slot immediately.
        Set<LocalTime> bookedTimes = appointmentService.getActiveTimesForDay(date)
                .stream()
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toSet());

        List<AvailableSlotResponse> slots = new ArrayList<>();
        LocalTime current = WORK_START;

        while (!current.plusMinutes(slotDuration).isAfter(WORK_END)) {
            if (!bookedTimes.contains(current)) {
                slots.add(new AvailableSlotResponse(date, current, slotDuration));
            }
            current = current.plusMinutes(slotDuration);
        }

        return slots;
    }
}
