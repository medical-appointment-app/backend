package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import medical.app.backend.common.usecase.ExecuteUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CancelAppointmentUseCase extends ExecuteUseCase {

    private final AppointmentService appointmentService;

    public CancelAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public TransactionResult<AppointmentResponse> execute(CancelAppointmentRequest request,
                                                          String patientUserId) {
        return run(() -> {
            AppointmentResponse cancelled = appointmentService.cancel(request, patientUserId);

            return TransactionResult.success(
                    messages.get("appointment.cancel.success"),
                    cancelled,
                    List.of(
                            UiElement.text(
                                    messages.get("appointment.cancel.title"),
                                    messages.get("appointment.cancel.body", cancelled.scheduledAt())),
                            UiElement.navigateButton(
                                    messages.get("appointment.cancel.bookAnother"),
                                    "appointments"),
                            UiElement.homeButton()
                    ));
        });
    }

    @Override
    protected String technicalFailureMessageKey() {
        return "appointment.cancel.technicalFailure";
    }
}
