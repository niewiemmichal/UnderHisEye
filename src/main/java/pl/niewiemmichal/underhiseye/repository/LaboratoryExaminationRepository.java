package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.LaboratoryExamination;

public interface LaboratoryExaminationRepository extends JpaRepository<LaboratoryExamination, Long> {
}
