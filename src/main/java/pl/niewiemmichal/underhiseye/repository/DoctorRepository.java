package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.repository.CrudRepository;
import pl.niewiemmichal.underhiseye.model.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
}
