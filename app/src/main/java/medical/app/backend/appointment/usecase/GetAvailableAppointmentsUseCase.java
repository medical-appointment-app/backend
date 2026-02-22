package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.service.AppointmentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAvailableAppointmentsUseCase {

    private final AppointmentService appointmentService;

    public GetAvailableAppointmentsUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public List<AppointmentResponse> executeForDay(DayAppointmentsQuery query) {
        return appointmentService.getAvailableAppointmentsForDay(query);
    }

    public List<AppointmentResponse> executeForWeek(WeekAppointmentsQuery query) {
        return appointmentService.getAvailableAppointmentsForWeek(query);
    }

    public List<AppointmentResponse> executeForMonth(MonthAppointmentsQuery query) {
        return appointmentService.getAvailableAppointmentsForMonth(query);
    }
}
