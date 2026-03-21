package medical.app.backend.doctor.service;

import medical.app.backend.doctor.dto.DoctorResponse;

public interface DoctorService {

    DoctorResponse getDoctor();

    /**
     * Returns the doctor's configured slot duration in minutes.
     * Falls back to 30 if no doctor record exists or slot duration is not set.
     */
    int getSlotDurationMinutes();
}
