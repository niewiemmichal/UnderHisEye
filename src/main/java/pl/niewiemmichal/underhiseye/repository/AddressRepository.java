package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.niewiemmichal.underhiseye.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
