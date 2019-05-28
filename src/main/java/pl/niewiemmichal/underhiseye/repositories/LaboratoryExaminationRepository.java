package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.niewiemmichal.underhiseye.entities.LaboratoryExamStatus;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import java.util.List;

public interface LaboratoryExaminationRepository extends JpaRepository<LaboratoryExamination, Long> {
    List<LaboratoryExamination> findAllByVisit_Id(Long visitId);
    List<LaboratoryExamination> findAllByStatus(LaboratoryExamStatus status);
}
