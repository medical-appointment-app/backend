package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.service.AppointmentService;
import org.springframework.stereotype.Component;

@Component
public class ConfirmAppointmentUseCase {

    private final AppointmentService appointmentService;

    public ConfirmAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public AppointmentResponse execute(Long appointmentId, String patientUserId) {
        return appointmentService.confirmAppointment(appointmentId, patientUserId);
    }
}
