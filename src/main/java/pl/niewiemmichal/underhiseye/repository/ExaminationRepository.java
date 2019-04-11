package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.repository.CrudRepository;
import pl.niewiemmichal.underhiseye.model.Examination;

public interface ExaminationRepository extends CrudRepository<Examination,String> {
}
