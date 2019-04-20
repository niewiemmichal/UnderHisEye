package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Examination;

public interface ExaminationRepository extends JpaRepository<Examination,String> {
}
