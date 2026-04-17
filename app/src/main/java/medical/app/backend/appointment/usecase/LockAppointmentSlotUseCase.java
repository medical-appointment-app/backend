package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.LockSlotRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import medical.app.backend.common.usecase.ExecuteUseCase;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LockAppointmentSlotUseCase extends ExecuteUseCase {

    /** How long (in minutes) a lock is held before it expires. */
    private static final int LOCK_TTL_MINUTES = 10;

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public LockAppointmentSlotUseCase(AppointmentService appointmentService,
                                      DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    public TransactionResult<AppointmentResponse> execute(String patientUserId, LockSlotRequest request) {
        return run(() -> {
            int slotDuration = doctorService.getSlotDurationMinutes();
            AppointmentResponse reserved = appointmentService.lockSlot(
                    patientUserId, request, slotDuration, LOCK_TTL_MINUTES);

            return TransactionResult.created(
                    "Slot reserved — please confirm within " + LOCK_TTL_MINUTES + " minutes.",
                    reserved,
                    List.of(
                            UiElement.text("Your hold",
                                    "Expires at " + reserved.lockedUntil() + "."),
                            UiElement.navigateButton("Confirm booking", "appointments/" + reserved.id() + "/confirm"),
                            UiElement.backButton()
                    ));
        });
    }

    @Override
    protected String technicalFailureMessage() {
        return "We couldn't reserve this slot. Please try again.";
    }
}
