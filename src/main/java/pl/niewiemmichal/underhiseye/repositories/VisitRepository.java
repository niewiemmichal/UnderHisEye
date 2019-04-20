package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
