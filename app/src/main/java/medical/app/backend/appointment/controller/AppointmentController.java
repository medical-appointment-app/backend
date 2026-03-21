package medical.app.backend.appointment.controller;

import jakarta.validation.Valid;
import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.AvailableSlotResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.usecase.BookAppointmentUseCase;
import medical.app.backend.appointment.usecase.CancelAppointmentUseCase;
import medical.app.backend.appointment.usecase.GetAvailableAppointmentsUseCase;
import medical.app.backend.appointment.usecase.GetPatientAppointmentsUseCase;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.web.ResponseBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final BookAppointmentUseCase bookAppointmentUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final GetPatientAppointmentsUseCase getPatientAppointmentsUseCase;
    private final GetAvailableAppointmentsUseCase getAvailableAppointmentsUseCase;

    public AppointmentController(
            BookAppointmentUseCase bookAppointmentUseCase,
            CancelAppointmentUseCase cancelAppointmentUseCase,
            GetPatientAppointmentsUseCase getPatientAppointmentsUseCase,
            GetAvailableAppointmentsUseCase getAvailableAppointmentsUseCase) {
        this.bookAppointmentUseCase = bookAppointmentUseCase;
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
        this.getPatientAppointmentsUseCase = getPatientAppointmentsUseCase;
        this.getAvailableAppointmentsUseCase = getAvailableAppointmentsUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody CreateAppointmentRequest request) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.created(bookAppointmentUseCase.execute(userId, request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments() {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.ok(getPatientAppointmentsUseCase.execute(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getById(@PathVariable Long id) {
        return ResponseBuilder.ok(getPatientAppointmentsUseCase.executeById(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancel(@PathVariable Long id) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.ok(cancelAppointmentUseCase.execute(new CancelAppointmentRequest(id), userId));
    }

    // ── Booked appointments (what is already taken) ────────────────────────

    @GetMapping("/booked/day")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getBookedForDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeBookedForDay(new DayAppointmentsQuery(date)));
    }

    @GetMapping("/booked/week")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getBookedForWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeBookedForWeek(new WeekAppointmentsQuery(weekStart)));
    }

    @GetMapping("/booked/month")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getBookedForMonth(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeBookedForMonth(new MonthAppointmentsQuery(year, month)));
    }

    // ── Free slots (what can still be booked) ──────────────────────────────

    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeSlotsForDay(date));
    }
}
