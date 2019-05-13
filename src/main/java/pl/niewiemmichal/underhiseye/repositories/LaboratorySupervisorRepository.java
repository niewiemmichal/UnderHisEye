package pl.niewiemmichal.underhiseye.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.niewiemmichal.underhiseye.entities.LaboratorySupervisor;

public interface LaboratorySupervisorRepository extends JpaRepository<LaboratorySupervisor, Long> {
    Optional<LaboratorySupervisor> findByUser_Username(String username);
}