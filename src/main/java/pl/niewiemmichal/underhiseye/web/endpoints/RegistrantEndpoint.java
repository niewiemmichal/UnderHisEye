package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("registrants")
public class RegistrantEndpoint {

    private final RegistrantRepository registrantRepository;
    private final RegistrationService registrationService;

    RegistrantEndpoint(RegistrantRepository registrantRepository, RegistrationService registrationService) {
        this.registrantRepository = registrantRepository;
        this.registrationService = registrationService;
    }

    @RolesAllowed({"REGISTRANT", "ADMINISTRATOR"})
    @GetMapping("/{id}")
    public Registrant getPatientRegistrationSpecialist(@PathVariable Long id){
        return registrantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("PatientRegistrationSpecialistEndpoint", "id",
                        id.toString()));
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<Registrant> getAllPatientRegistrationSpecialists(){
        return registrantRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public Registrant addPatientRegistrationSpecialist(@Valid @RequestBody NewUserDto newRegistrant){
        return registrationService.registerRegistrant(newRegistrant);
    }
}
