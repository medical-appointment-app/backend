package medical.app.backend.appointment.service;

import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.LockSlotRequest;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.enums.AppointmentStatus;
import medical.app.backend.appointment.model.Appointment;
import medical.app.backend.appointment.repository.AppointmentRepository;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.common.i18n.Messages;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;
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
    private final Messages messages;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, Messages messages) {
        this.appointmentRepository = appointmentRepository;
        this.messages = messages;
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
                Objects.requireNonNullElse(request.durationMinutes(), defaultSlotDurationMinutes));
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse lockSlot(String patientUserId, LockSlotRequest request,
                                        int slotDurationMinutes, int lockTtlMinutes) {
        LocalDateTime now = LocalDateTime.now();

        // Pessimistic write lock — concurrent transactions wait here instead of racing.
        boolean slotTaken = appointmentRepository.findActiveSlotForLocking(
                request.scheduledAt(),
                AppointmentStatus.CANCELLED,
                AppointmentStatus.LOCKED,
                now).isPresent();

        if (slotTaken) {
            throw new IllegalStateException(messages.get("appointment.slot.taken"));
        }

        Appointment appointment = new Appointment();
        appointment.setPatientUserId(patientUserId);
        appointment.setDoctorId(request.doctorId());
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setNotes(request.notes());
        appointment.setDurationMinutes(slotDurationMinutes);
        appointment.setStatus(AppointmentStatus.LOCKED);
        appointment.setLockedUntil(now.plusMinutes(lockTtlMinutes));

        try {
            return AppointmentResponse.from(appointmentRepository.saveAndFlush(appointment));
        } catch (DataIntegrityViolationException e) {
            // Partial unique index fires when two transactions race past the check above.
            throw new IllegalStateException(messages.get("appointment.slot.taken"));
        }
    }

    @Override
    public AppointmentResponse confirmAppointment(Long appointmentId, String patientUserId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        if (!appointment.getPatientUserId().equals(patientUserId)) {
            throw new UnauthorizedException(messages.get("appointment.unauthorized.confirm"));
        }

        if (appointment.getStatus() != AppointmentStatus.LOCKED) {
            throw new IllegalStateException(
                    messages.get("appointment.invalidStatus.confirm", appointment.getStatus()));
        }

        if (appointment.getLockedUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(messages.get("appointment.hold.expired"));
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setLockedUntil(null);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    public int releaseExpiredLocks() {
        return appointmentRepository.releaseExpiredLocks(
                AppointmentStatus.LOCKED,
                AppointmentStatus.CANCELLED,
                LocalDateTime.now());
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
            throw new UnauthorizedException(messages.get("appointment.unauthorized.cancel"));
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setLockedUntil(null);
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
    public List<LocalDateTime> getActiveTimesForDay(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to   = date.plusDays(1).atStartOfDay();
        return appointmentRepository.findActiveScheduledTimesBetween(
                from, to,
                AppointmentStatus.CANCELLED,
                AppointmentStatus.LOCKED,
                LocalDateTime.now());
    }

    private List<AppointmentResponse> fetchExcludingCancelled(LocalDateTime from, LocalDateTime to) {
        return appointmentRepository
                .findByScheduledAtBetweenAndStatusNot(from, to, AppointmentStatus.CANCELLED)
                .stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
