package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Registrant;

public interface RegistrantRepository extends JpaRepository<Registrant, Long> {
}
