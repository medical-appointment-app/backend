package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import org.springframework.stereotype.Component;

@Component
public class BookAppointmentUseCase {

    private final AppointmentService appointmentService;

    public BookAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public AppointmentResponse execute(String patientUserId, CreateAppointmentRequest request) {
        return appointmentService.create(patientUserId, request);
    }
}
