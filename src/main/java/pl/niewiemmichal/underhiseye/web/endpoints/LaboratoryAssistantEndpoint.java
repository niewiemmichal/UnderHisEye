package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.model.LaboratoryAssistant;
import pl.niewiemmichal.underhiseye.repository.LaboratoryAssistantRepository;

import java.util.List;


@RequestMapping ("/laboratoryassistants")
@RestController
public class LaboratoryAssistantEndpoint {

    private final LaboratoryAssistantRepository laboratoryAssistantRepository;

    LaboratoryAssistantEndpoint(LaboratoryAssistantRepository laboratoryAssistantRepository) {
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
    }

    @GetMapping ("/{id}")
    public LaboratoryAssistant getLaboratoryAssistant(@PathVariable Long id){
        return laboratoryAssistantRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("LaboratoryAssistant","id",id.toString()));
    }

    @GetMapping
    public List<LaboratoryAssistant> getAllLaboratoryAssistants(){
        return laboratoryAssistantRepository.findAll();
    }

    @PostMapping
    public LaboratoryAssistant addLaboratoryAssistant(@RequestBody LaboratoryAssistant newLaboratoryAssistant){
        return laboratoryAssistantRepository.save(newLaboratoryAssistant);
    }

    @PutMapping("/{id}")
    public LaboratoryAssistant updateLaboratoryAssistant(@RequestBody LaboratoryAssistant newLaboratoryAssistant, @PathVariable Long id){
        if(!laboratoryAssistantRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("LaboratoryAssistant", "id", id.toString());
        else if(newLaboratoryAssistant.getId() != null && !(id.equals(newLaboratoryAssistant.getId())))
            throw new ResourceConflictException("LaboratoryAssistant", "id", id.toString(), newLaboratoryAssistant.getId().toString());
        else {
            newLaboratoryAssistant.setId(id);
            return laboratoryAssistantRepository.save(newLaboratoryAssistant);
        }
    }
}
