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
                    "Your appointment is confirmed.",
                    confirmed,
                    List.of(
                            UiElement.text("Confirmation",
                                    "See you on " + confirmed.scheduledAt() + "."),
                            UiElement.navigateButton("View my appointments", "my-appointments"),
                            UiElement.homeButton()
                    ));
        });
    }

    @Override
    protected String technicalFailureMessage() {
        return "We couldn't confirm your appointment. Please try again.";
    }
}
