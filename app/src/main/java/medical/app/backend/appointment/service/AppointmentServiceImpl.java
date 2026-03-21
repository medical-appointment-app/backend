package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.enums.AppointmentStatus;
import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.repository.AppointmentRepository;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse create(String patientUserId, CreateAppointmentRequest request,
                                      int defaultSlotDurationMinutes) {
        Appointment appointment = new Appointment();
        appointment.setPatientUserId(patientUserId);
        appointment.setDoctorId(request.doctorId());
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setNotes(request.notes());
        appointment.setDurationMinutes(
                request.durationMinutes() != null
                        ? request.durationMinutes()
                        : defaultSlotDurationMinutes);
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
    public AppointmentResponse cancel(CancelAppointmentRequest request, String patientUserId) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", request.appointmentId()));

        if (!appointment.getPatientUserId().equals(patientUserId)) {
            throw new UnauthorizedException("You are not authorised to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getBookedAppointmentsForDay(DayAppointmentsQuery query) {
        LocalDateTime from = query.date().atStartOfDay();
        LocalDateTime to   = query.date().plusDays(1).atStartOfDay();
        return fetchExcludingCancelled(from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getBookedAppointmentsForWeek(WeekAppointmentsQuery query) {
        LocalDateTime from = query.weekStart().atStartOfDay();
        LocalDateTime to   = query.weekStart().plusWeeks(1).atStartOfDay();
        return fetchExcludingCancelled(from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getBookedAppointmentsForMonth(MonthAppointmentsQuery query) {
        YearMonth yearMonth = YearMonth.of(query.year(), query.month());
        LocalDateTime from  = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime to    = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();
        return fetchExcludingCancelled(from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getBookedTimesForDay(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to   = date.plusDays(1).atStartOfDay();
        return appointmentRepository
                .findByScheduledAtBetweenAndStatusNot(from, to, AppointmentStatus.CANCELLED)
                .stream()
                .map(Appointment::getScheduledAt)
                .toList();
    }

    private List<AppointmentResponse> fetchExcludingCancelled(LocalDateTime from, LocalDateTime to) {
        return appointmentRepository
                .findByScheduledAtBetweenAndStatusNot(from, to, AppointmentStatus.CANCELLED)
                .stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
