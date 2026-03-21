package medical.app.backend.doctor.usecase;

import medical.app.backend.doctor.dto.DoctorResponse;
import medical.app.backend.doctor.service.DoctorService;
import org.springframework.stereotype.Component;

@Component
public class GetDoctorUseCase {

    private final DoctorService doctorService;

    public GetDoctorUseCase(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public DoctorResponse execute() {
        return doctorService.getDoctor();
    }
}
