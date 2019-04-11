package pl.niewiemmichal.underhiseye.repository;

import org.springframework.data.repository.CrudRepository;
import pl.niewiemmichal.underhiseye.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long>{
}
