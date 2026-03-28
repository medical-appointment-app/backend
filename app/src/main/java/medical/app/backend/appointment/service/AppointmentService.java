package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.LockSlotRequest;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(String patientUserId, CreateAppointmentRequest request, int defaultSlotDurationMinutes);

    /**
     * Reserves a slot for {@code patientUserId} for {@code lockTtlMinutes}.
     * Throws {@link IllegalStateException} (→ HTTP 409) if the slot is already taken.
     */
    AppointmentResponse lockSlot(String patientUserId, LockSlotRequest request,
                                 int slotDurationMinutes, int lockTtlMinutes);

    /**
     * Converts a LOCKED appointment to CONFIRMED.
     * Throws if the appointment does not belong to the caller or the lock has expired.
     */
    AppointmentResponse confirmAppointment(Long appointmentId, String patientUserId);

    /** Bulk-releases expired LOCKED appointments (called by the cleanup scheduler). */
    int releaseExpiredLocks();

    List<AppointmentResponse> getByPatient(String patientUserId);

    AppointmentResponse getById(Long id);

    AppointmentResponse cancel(CancelAppointmentRequest request, String patientUserId);

    List<AppointmentResponse> getBookedAppointmentsForDay(DayAppointmentsQuery query);

    List<AppointmentResponse> getBookedAppointmentsForWeek(WeekAppointmentsQuery query);

    List<AppointmentResponse> getBookedAppointmentsForMonth(MonthAppointmentsQuery query);

    /**
     * Returns the scheduled times of all active (non-expired) appointments on the given day.
     * Used by the slot-availability calculator; expired locks are excluded so those slots
     * become visible again.
     */
    List<LocalDateTime> getActiveTimesForDay(LocalDate date);
}
