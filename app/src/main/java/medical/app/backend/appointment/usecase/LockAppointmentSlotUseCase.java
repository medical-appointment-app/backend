package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.LockSlotRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

@Component
public class LockAppointmentSlotUseCase {

    /** How long (in minutes) a lock is held before it expires. */
    private static final int LOCK_TTL_MINUTES = 10;

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public LockAppointmentSlotUseCase(AppointmentService appointmentService,
                                      DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    public AppointmentResponse execute(String patientUserId, LockSlotRequest request) {
        int slotDuration = doctorService.getSlotDurationMinutes();
        return appointmentService.lockSlot(patientUserId, request, slotDuration, LOCK_TTL_MINUTES);
    }
}
