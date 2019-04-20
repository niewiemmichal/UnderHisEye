package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
