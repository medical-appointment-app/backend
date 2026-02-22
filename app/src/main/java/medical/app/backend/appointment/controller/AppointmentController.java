package medical.app.backend.appointment.controller;

import jakarta.validation.Valid;
import medical.app.backend.appointment.dto.AppointmentResponse;
import medical.app.backend.appointment.dto.CreateAppointmentRequest;
import medical.app.backend.appointment.service.AppointmentService;
import medical.app.backend.common.context.RequestContext;
import medical.app.backend.common.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody CreateAppointmentRequest request) {
        String userId = RequestContext.getSessionInfo().userId();
        AppointmentResponse response = appointmentService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments() {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getByPatient(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getById(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancel(@PathVariable Long id) {
        String userId = RequestContext.getSessionInfo().userId();
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.cancel(id, userId)));
    }
}
