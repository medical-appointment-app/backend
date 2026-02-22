package medical.app.backend.appointment.controller;

import jakarta.validation.Valid;
import medical.app.backend.appointment.dto.AppointmentResponse;
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

    @GetMapping("/available/day")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAvailableForDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeForDay(new DayAppointmentsQuery(date)));
    }

    @GetMapping("/available/week")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAvailableForWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeForWeek(new WeekAppointmentsQuery(weekStart)));
    }

    @GetMapping("/available/month")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAvailableForMonth(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseBuilder.ok(getAvailableAppointmentsUseCase.executeForMonth(new MonthAppointmentsQuery(year, month)));
    }
}
