package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.PhysicalExamination;

public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExamination, Long> {
}
