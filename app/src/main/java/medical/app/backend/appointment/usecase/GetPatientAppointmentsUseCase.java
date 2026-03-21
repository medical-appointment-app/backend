package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.exception.UnauthorizedException;
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
        AppointmentResponse response = appointmentService.getById(id);
        String requestingUserId = RequestContext.getSessionInfo().userId();
        if (!response.patientUserId().equals(requestingUserId)) {
            throw new UnauthorizedException("You are not authorised to view this appointment");
        }
        return response;
    }
}
