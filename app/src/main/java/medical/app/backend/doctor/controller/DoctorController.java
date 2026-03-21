package medical.app.backend.doctor.controller;

import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.web.ResponseBuilder;
import medical.app.backend.doctor.dto.DoctorResponse;
import medical.app.backend.doctor.usecase.GetDoctorUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final GetDoctorUseCase getDoctorUseCase;

    public DoctorController(GetDoctorUseCase getDoctorUseCase) {
        this.getDoctorUseCase = getDoctorUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor() {
        return ResponseBuilder.ok(getDoctorUseCase.execute());
    }
}
