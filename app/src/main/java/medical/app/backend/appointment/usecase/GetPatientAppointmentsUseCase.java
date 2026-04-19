package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.common.i18n.Messages;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetPatientAppointmentsUseCase {

    private final AppointmentService appointmentService;
    private final Messages messages;

    public GetPatientAppointmentsUseCase(AppointmentService appointmentService, Messages messages) {
        this.appointmentService = appointmentService;
        this.messages = messages;
    }

    public List<AppointmentResponse> execute(String patientUserId) {
        return appointmentService.getByPatient(patientUserId);
    }

    public AppointmentResponse executeById(Long id) {
        AppointmentResponse response = appointmentService.getById(id);
        String requestingUserId = RequestContext.getSessionInfo().userId();
        if (!response.patientUserId().equals(requestingUserId)) {
            throw new UnauthorizedException(messages.get("appointment.unauthorized.view"));
        }
        return response;
    }
}
