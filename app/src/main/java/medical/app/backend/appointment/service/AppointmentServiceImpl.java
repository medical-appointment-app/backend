package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.model.AppointmentStatus;
import medical.app.backend.appointment.repository.AppointmentRepository;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse create(String patientUserId, CreateAppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setPatientUserId(patientUserId);
        appointment.setDoctorId(request.doctorId());
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setNotes(request.notes());
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByPatient(String patientUserId) {
        return appointmentRepository.findByPatientUserId(patientUserId)
                .stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
    }

    @Override
    public AppointmentResponse cancel(Long id, String patientUserId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        if (!appointment.getPatientUserId().equals(patientUserId)) {
            throw new UnauthorizedException("You are not authorised to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }
}
