package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.service.AppointmentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetPatientAppointmentsUseCase {

    private final AppointmentService appointmentService;

    public GetPatientAppointmentsUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public List<AppointmentResponse> execute(String patientUserId) {
        return appointmentService.getByPatient(patientUserId);
    }

    public AppointmentResponse executeById(Long id) {
        return appointmentService.getById(id);
    }
}
