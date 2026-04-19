package medical.app.backend.doctor.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.doctor.dto.DoctorResponse;
import medical.app.backend.doctor.repository.DoctorRepository;

@Service
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

    private static final int DEFAULT_SLOT_DURATION_MINUTES = 30;

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorResponse getDoctor() {
        return doctorRepository.findAll().stream()
                .findFirst()
                .map(DoctorResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No doctor profile found"));
    }

    @Override
    public int getSlotDurationMinutes() {
        return doctorRepository.findAll().stream()
                .findFirst()
                .map(d -> d != null && d.getSlotDurationMinutes() > 0
                        ? d.getSlotDurationMinutes()
                        : DEFAULT_SLOT_DURATION_MINUTES)
                .orElse(DEFAULT_SLOT_DURATION_MINUTES);
    }
}
