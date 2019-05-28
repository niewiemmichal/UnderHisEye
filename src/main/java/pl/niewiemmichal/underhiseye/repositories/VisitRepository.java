package pl.niewiemmichal.underhiseye.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByDoctor_Id(Long doctorId);
}
