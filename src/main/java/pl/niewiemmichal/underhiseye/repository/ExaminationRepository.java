package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.Examination;

public interface ExaminationRepository extends JpaRepository<Examination,String> {
}
