package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAdministrator;
import pl.niewiemmichal.underhiseye.commons.annotations.IsRegistrant;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.LaboratorySupervisor;
import pl.niewiemmichal.underhiseye.repositories.LaboratorySupervisorRepository;

import java.util.List;

@Controller
@RequestMapping("laboratorySupervisors")
@ResponseBody
public class LaboratorySupervisorEndpoint {

    private final LaboratorySupervisorRepository laboratorySupervisorRepository;

    LaboratorySupervisorEndpoint(LaboratorySupervisorRepository laboratorySupervisorRepository) {
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
    }

    @IsAdministrator @IsRegistrant
    @GetMapping("/{id}")
    public LaboratorySupervisor getLaboratorySupervisor(@PathVariable Long id){
        return laboratorySupervisorRepository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException("LaboratorySupervisor","id",id.toString()));
    }

    @IsAdministrator
    @GetMapping
    public List<LaboratorySupervisor> getAllLaboratorySupervisors(){
        return (List<LaboratorySupervisor>) laboratorySupervisorRepository.findAll();
    }

    @IsAdministrator
    @PostMapping
    public LaboratorySupervisor addLaboratorySupervisor(@RequestBody LaboratorySupervisor newLaboratorySupervisor){
        return laboratorySupervisorRepository.save(newLaboratorySupervisor);
    }
}