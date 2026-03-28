package medical.app.backend.appointment.controller;

import jakarta.validation.Valid;
import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.AvailableSlotResponse;
import medical.app.backend.appointment.dto.CancelAppointmentRequest;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.dto.DayAppointmentsQuery;
import medical.app.backend.appointment.dto.LockSlotRequest;
import medical.app.backend.appointment.dto.MonthAppointmentsQuery;
import medical.app.backend.appointment.dto.WeekAppointmentsQuery;
import medical.app.backend.appointment.usecase.BookAppointmentUseCase;
import medical.app.backend.appointment.usecase.CancelAppointmentUseCase;
import medical.app.backend.appointment.usecase.ConfirmAppointmentUseCase;
import medical.app.backend.appointment.usecase.GetAvailableAppointmentsUseCase;
import medical.app.backend.appointment.usecase.GetPatientAppointmentsUseCase;
import medical.app.backend.appointment.usecase.LockAppointmentSlotUseCase;
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
    private final LockAppointmentSlotUseCase lockAppointmentSlotUseCase;
    private final ConfirmAppointmentUseCase confirmAppointmentUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final GetPatientAppointmentsUseCase getPatientAppointmentsUseCase;
    private final GetAvailableAppointmentsUseCase getAvailableAppointmentsUseCase;

    public AppointmentController(
            BookAppointmentUseCase bookAppointmentUseCase,
            LockAppointmentSlotUseCase lockAppointmentSlotUseCase,
            ConfirmAppointmentUseCase confirmAppointmentUseCase,
            CancelAppointmentUseCase cancelAppointmentUseCase,
            GetPatientAppointmentsUseCase getPatientAppointmentsUseCase,
            GetAvailableAppointmentsUseCase getAvailableAppointmentsUseCase) {
        this.bookAppointmentUseCase = bookAppointmentUseCase;
        this.lockAppointmentSlotUseCase = lockAppointmentSlotUseCase;
        this.confirmAppointmentUseCase = confirmAppointmentUseCase;
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
        this.getPatientAppointmentsUseCase = getPatientAppointmentsUseCase;
        this.getAvailableAppointmentsUseCase = getAvailableAppointmentsUseCase;
    }

    // ── Two-step booking flow ─────────────────────────────────────────────
    // 1. POST /reserve  — patient selects a slot (holds it for 10 min)
    // 2. POST /{id}/confirm — patient finalises the booking

    /**
     * Step 1 — Patient selects a slot on the approval page.
     * Internally reserves the slot for 10 minutes; returns a lockedUntil timestamp
     * so the client can show a countdown before the hold expires.
     * HTTP 409 if another patient has already taken the slot.
     */
    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<AppointmentResponse>> reserve(
            @Valid @RequestBody LockSlotRequest request) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.created(lockAppointmentSlotUseCase.execute(userId, request));
    }

    /**
     * Step 2 — Confirm a previously locked appointment.
     * HTTP 409 if the lock has expired or the slot is in an invalid state.
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirm(@PathVariable Long id) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.ok(confirmAppointmentUseCase.execute(id, userId));
    }

    // ── Direct booking (admin / legacy) ───────────────────────────────────

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody CreateAppointmentRequest request) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseBuilder.created(bookAppointmentUseCase.execute(userId, request));
    }

    // ── Patient appointment queries ────────────────────────────────────────

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

    // ── Free slots (what can still be booked) ─────────────────────────────

    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeSlotsForDay(date));
    }
}
