package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

import java.util.List;
import java.util.Optional;

public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExamination, Long> {
    List<PhysicalExamination> findAllByVisit_Id(Long visitId);;
}
