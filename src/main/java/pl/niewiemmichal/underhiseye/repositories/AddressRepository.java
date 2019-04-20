package pl.niewiemmichal.underhiseye.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
