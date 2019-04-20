package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExamination, Long> {
}
