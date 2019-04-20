package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
