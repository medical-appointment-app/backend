package medical.app.backend.doctor.repository;

import medical.app.backend.common.model.person.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
