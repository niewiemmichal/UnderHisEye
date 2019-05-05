package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.LaboratorySupervisor;
import pl.niewiemmichal.underhiseye.repositories.LaboratorySupervisorRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("supervisors")
public class LaboratorySupervisorEndpoint {

    private final LaboratorySupervisorRepository laboratorySupervisorRepository;
    private final RegistrationService registrationService;

    @Autowired
    LaboratorySupervisorEndpoint(LaboratorySupervisorRepository laboratorySupervisorRepository,
                                 RegistrationService registrationService) {
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.registrationService = registrationService;
    }

    @RolesAllowed({"REGISTRANT", "ADMINISTRATOR"})
    @GetMapping("/{id}")
    public LaboratorySupervisor getLaboratorySupervisor(@PathVariable Long id){
        return laboratorySupervisorRepository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException("LaboratorySupervisor","id",id.toString()));
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<LaboratorySupervisor> getAllLaboratorySupervisors(){
        return laboratorySupervisorRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public LaboratorySupervisor addLaboratorySupervisor(@Valid @RequestBody NewUserDto newSupervisor){
        return registrationService.registerSupervisor(newSupervisor);
    }
}