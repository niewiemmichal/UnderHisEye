package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.PatientRegistrationSpecialist;

public interface PatientRegistrationSpecialistRepository extends JpaRepository<PatientRegistrationSpecialist, Long> {
}
