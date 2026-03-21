package medical.app.backend.doctor.dto;

import medical.app.backend.common.model.person.Doctor;

public record DoctorResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer slotDurationMinutes) {

    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getSlotDurationMinutes()
        );
    }
}
