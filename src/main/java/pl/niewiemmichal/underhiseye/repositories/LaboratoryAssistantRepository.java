package pl.niewiemmichal.underhiseye.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.niewiemmichal.underhiseye.entities.LaboratoryAssistant;

public interface LaboratoryAssistantRepository extends JpaRepository<LaboratoryAssistant, Long> {
    Optional<LaboratoryAssistant> findByUser_Username(String username);
}
