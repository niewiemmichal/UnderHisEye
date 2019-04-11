package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.repository.CrudRepository;
import pl.niewiemmichal.underhiseye.model.Visit;

public interface VisitRepository extends CrudRepository<Visit, Long> {
}
