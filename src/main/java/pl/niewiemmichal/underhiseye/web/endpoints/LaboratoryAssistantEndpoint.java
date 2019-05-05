package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.entities.LaboratoryAssistant;
import pl.niewiemmichal.underhiseye.repositories.LaboratoryAssistantRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@RestController
@RequestMapping ("assistants")
public class LaboratoryAssistantEndpoint {

    private final LaboratoryAssistantRepository laboratoryAssistantRepository;
    private final RegistrationService registrationService;

    @Autowired
    LaboratoryAssistantEndpoint(LaboratoryAssistantRepository laboratoryAssistantRepository,
                                RegistrationService registrationService) {
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
        this.registrationService = registrationService;
    }

    @RolesAllowed({"ASSISTANT", "ADMINISTRATOR"})
    @GetMapping ("/{id}")
    public LaboratoryAssistant getLaboratoryAssistant(@PathVariable Long id){
        return laboratoryAssistantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("LaboratoryAssistant", "id", id.toString()));
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<LaboratoryAssistant> getAllLaboratoryAssistants(){
        return laboratoryAssistantRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public LaboratoryAssistant addLaboratoryAssistant(@Valid @RequestBody NewUserDto newAssistant){
        return registrationService.registerAssistant(newAssistant);
    }
}
