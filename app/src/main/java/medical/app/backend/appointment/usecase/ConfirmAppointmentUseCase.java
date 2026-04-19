package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import medical.app.backend.common.usecase.ExecuteUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfirmAppointmentUseCase extends ExecuteUseCase {

    private final AppointmentService appointmentService;

    public ConfirmAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public TransactionResult<AppointmentResponse> execute(Long appointmentId, String patientUserId) {
        return run(() -> {
            AppointmentResponse confirmed =
                    appointmentService.confirmAppointment(appointmentId, patientUserId);

            return TransactionResult.success(
                    messages.get("appointment.confirm.success"),
                    confirmed,
                    List.of(
                            UiElement.text(
                                    messages.get("appointment.confirm.title"),
                                    messages.get("appointment.confirm.body", confirmed.scheduledAt())),
                            UiElement.navigateButton(
                                    messages.get("appointment.confirm.viewMine"),
                                    "my-appointments"),
                            UiElement.homeButton()
                    ));
        });
    }

    @Override
    protected String technicalFailureMessageKey() {
        return "appointment.confirm.technicalFailure";
    }
}
