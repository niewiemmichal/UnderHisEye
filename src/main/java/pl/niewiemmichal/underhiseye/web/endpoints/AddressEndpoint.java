package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.model.Address;
import pl.niewiemmichal.underhiseye.repository.AddressRepository;

import java.util.List;

@RequestMapping ("/addresses")
@RestController
public class AddressEndpoint {

    private final AddressRepository addressRepository;

    AddressEndpoint(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping ("/{id}")
    public Address getAddress(@PathVariable Long id){
        return addressRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("Address","id",id.toString()));
    }

    @GetMapping
    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    @PostMapping
    public Address addAddress(@RequestBody Address newAddress){
        return addressRepository.save(newAddress);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@RequestBody Address newAddress, @PathVariable Long id) {
        if(!addressRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Address", "id", id.toString());
        else if(newAddress.getId() != null && !(id.equals(newAddress.getId())))
            throw new ResourceConflictException("Address", "id", id.toString(), newAddress.getId().toString());
        else {
            newAddress.setId(id);
            return addressRepository.save(newAddress);
        }
    }
}
