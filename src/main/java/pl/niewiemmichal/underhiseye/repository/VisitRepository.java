package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
