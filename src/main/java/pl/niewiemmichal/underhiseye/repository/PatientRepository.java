package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.repository.CrudRepository;
import pl.niewiemmichal.underhiseye.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Long> {
}
