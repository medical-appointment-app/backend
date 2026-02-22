package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(String patientUserId, CreateAppointmentRequest request);

    List<AppointmentResponse> getByPatient(String patientUserId);

    AppointmentResponse getById(Long id);

    AppointmentResponse cancel(Long id, String patientUserId);
}
