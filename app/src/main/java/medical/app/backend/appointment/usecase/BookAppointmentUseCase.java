package medical.app.backend.appointment.usecase;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

@Component
public class BookAppointmentUseCase {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public BookAppointmentUseCase(AppointmentService appointmentService,
                                  DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    public AppointmentResponse execute(String patientUserId, CreateAppointmentRequest request) {
        int defaultSlotDuration = doctorService.getSlotDurationMinutes();
        return appointmentService.create(patientUserId, request, defaultSlotDuration);
    }
}
