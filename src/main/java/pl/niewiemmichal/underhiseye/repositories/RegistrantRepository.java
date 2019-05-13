package pl.niewiemmichal.underhiseye.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.niewiemmichal.underhiseye.entities.Registrant;

public interface RegistrantRepository extends JpaRepository<Registrant, Long> {
    Optional<Registrant> findByUser_Username(String username);
}
