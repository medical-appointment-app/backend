package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import medical.app.backend.common.usecase.ExecuteUseCase;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookAppointmentUseCase extends ExecuteUseCase {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public BookAppointmentUseCase(AppointmentService appointmentService,
                                  DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    public TransactionResult<AppointmentResponse> execute(String patientUserId,
                                                          CreateAppointmentRequest request) {
        return run(() -> {
            int defaultSlotDuration = doctorService.getSlotDurationMinutes();
            AppointmentResponse appointment =
                    appointmentService.create(patientUserId, request, defaultSlotDuration);

            return TransactionResult.created(
                    messages.get("appointment.book.success"),
                    appointment,
                    List.of(
                            UiElement.text(
                                    messages.get("appointment.book.confirmation.title"),
                                    messages.get("appointment.book.confirmation.body",
                                            appointment.scheduledAt())),
                            UiElement.navigateButton(
                                    messages.get("appointment.book.viewMine"),
                                    "my-appointments"),
                            UiElement.homeButton()
                    ));
        });
    }

    @Override
    protected String technicalFailureMessageKey() {
        return "appointment.book.technicalFailure";
    }
}
