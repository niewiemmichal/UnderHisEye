package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;

public interface LaboratoryExaminationRepository extends JpaRepository<LaboratoryExamination, Long> {
}
