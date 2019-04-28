package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAdministrator;
import pl.niewiemmichal.underhiseye.commons.annotations.IsRegistrant;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("registrants")
@ResponseBody
public class RegistrantEndpoint {

    private final RegistrantRepository registrantRepository;

    RegistrantEndpoint(RegistrantRepository registrantRepository) {
        this.registrantRepository = registrantRepository;
    }

    @IsAdministrator @IsRegistrant
    @GetMapping("/{id}")
    public Registrant getPatientRegistrationSpecialist(@PathVariable Long id){
        return registrantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("PatientRegistrationSpecialistEndpoint","id",id.toString()));
    }

    @IsAdministrator
    @GetMapping
    public List<Registrant> getAllPatientRegistrationSpecialists(){
        return (List<Registrant>) registrantRepository.findAll();
    }

    @IsAdministrator
    @PostMapping
    public Registrant addPatientRegistrationSpecialist(@RequestBody Registrant newRegistrant){
        return registrantRepository.save(newRegistrant);
    }
}
