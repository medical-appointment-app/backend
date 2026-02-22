package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import org.springframework.stereotype.Component;

@Component
public class CancelAppointmentUseCase {

    private final AppointmentService appointmentService;

    public CancelAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public AppointmentResponse execute(CancelAppointmentRequest request, String patientUserId) {
        return appointmentService.cancel(request, patientUserId);
    }
}
