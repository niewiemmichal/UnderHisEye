package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public LaboratorySupervisor getLaboratorySupervisor(@PathVariable Long id){
        return laboratorySupervisorRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("LaboratorySupervisor","id",id.toString()));
    }

    @GetMapping
    public List<LaboratorySupervisor> getAllLaboratorySupervisors(){
        return (List<LaboratorySupervisor>) laboratorySupervisorRepository.findAll();
    }

    @PostMapping
    public LaboratorySupervisor addLaboratorySupervisor(@RequestBody LaboratorySupervisor newLaboratorySupervisor){
        return laboratorySupervisorRepository.save(newLaboratorySupervisor);
    }

    @PutMapping("/{id}")
    public LaboratorySupervisor updateLaboratorySupervisor(@RequestBody LaboratorySupervisor newLaboratorySupervisor, @PathVariable Long id){
        if(!laboratorySupervisorRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("LaboratorySupervisor", "id", id.toString());
        else if(newLaboratorySupervisor.getId() != null && !(id.equals(newLaboratorySupervisor.getId())))
            throw new ResourceConflictException("LaboratorySupervisor", "id", id.toString(), newLaboratorySupervisor.getId().toString());
        else {
            newLaboratorySupervisor.setId(id);
            return laboratorySupervisorRepository.save(newLaboratorySupervisor);
        }
    }

}