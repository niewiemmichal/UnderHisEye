package pl.niewiemmichal.underhiseye.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser_Username(String username);
}
