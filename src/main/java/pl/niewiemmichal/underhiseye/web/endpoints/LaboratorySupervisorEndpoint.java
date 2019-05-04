package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.LaboratorySupervisor;
import pl.niewiemmichal.underhiseye.repositories.LaboratorySupervisorRepository;

import java.util.List;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("supervisors")
@ResponseBody
public class LaboratorySupervisorEndpoint {

    private final LaboratorySupervisorRepository laboratorySupervisorRepository;

    LaboratorySupervisorEndpoint(LaboratorySupervisorRepository laboratorySupervisorRepository) {
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
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
        return (List<LaboratorySupervisor>) laboratorySupervisorRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public LaboratorySupervisor addLaboratorySupervisor(@RequestBody LaboratorySupervisor newLaboratorySupervisor){
        return laboratorySupervisorRepository.save(newLaboratorySupervisor);
    }
}