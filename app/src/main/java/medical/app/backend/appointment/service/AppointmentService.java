package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(String patientUserId, CreateAppointmentRequest request);

    List<AppointmentResponse> getByPatient(String patientUserId);

    AppointmentResponse getById(Long id);

    AppointmentResponse cancel(CancelAppointmentRequest request, String patientUserId);

    List<AppointmentResponse> getAvailableAppointmentsForDay(DayAppointmentsQuery query);

    List<AppointmentResponse> getAvailableAppointmentsForWeek(WeekAppointmentsQuery query);

    List<AppointmentResponse> getAvailableAppointmentsForMonth(MonthAppointmentsQuery query);
}
