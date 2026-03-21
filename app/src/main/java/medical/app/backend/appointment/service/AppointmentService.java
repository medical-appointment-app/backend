package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(String patientUserId, CreateAppointmentRequest request, int defaultSlotDurationMinutes);

    List<AppointmentResponse> getByPatient(String patientUserId);

    AppointmentResponse getById(Long id);

    AppointmentResponse cancel(CancelAppointmentRequest request, String patientUserId);

    /** Returns non-cancelled appointments in the given time window (what is booked). */
    List<AppointmentResponse> getBookedAppointmentsForDay(DayAppointmentsQuery query);

    List<AppointmentResponse> getBookedAppointmentsForWeek(WeekAppointmentsQuery query);

    List<AppointmentResponse> getBookedAppointmentsForMonth(MonthAppointmentsQuery query);

    /** Returns the scheduledAt times of non-cancelled appointments on a given day (used for slot computation). */
    List<LocalDateTime> getBookedTimesForDay(LocalDate date);
}
