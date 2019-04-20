package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;

import java.util.List;

@Controller
@RequestMapping("patientRegistrationSpecialist")
@ResponseBody
public class RegistrantEndpoint {

    private final RegistrantRepository registrantRepository;

    RegistrantEndpoint(RegistrantRepository registrantRepository) {
        this.registrantRepository = registrantRepository;
    }

    @GetMapping("/{id}")
    public Registrant getPatientRegistrationSpecialist(@PathVariable Long id){
        return registrantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("PatientRegistrationSpecialistEndpoint","id",id.toString()));
    }

    @GetMapping
    public List<Registrant> getAllPatientRegistrationSpecialists(){
        return (List<Registrant>) registrantRepository.findAll();
    }

    @PostMapping
    public Registrant addPatientRegistrationSpecialist(@RequestBody Registrant newRegistrant){
        return registrantRepository.save(newRegistrant);
    }

    @PutMapping("/{id}")
    public Registrant updatePatientRegistrationSpecialist(@RequestBody Registrant newRegistrant, @PathVariable Long id){
        if(!registrantRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Registrant", "id", id.toString());
        else if(newRegistrant.getId() != null && !(id.equals(newRegistrant.getId())))
            throw new ResourceConflictException("Registrant", "id", id.toString(), newRegistrant.getId().toString());
        else {
            newRegistrant.setId(id);
            return registrantRepository.save(newRegistrant);
        }
    }

}
