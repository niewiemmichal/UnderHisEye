package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAdministrator;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAssistant;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.entities.LaboratoryAssistant;
import pl.niewiemmichal.underhiseye.repositories.LaboratoryAssistantRepository;

import java.util.List;


@RequestMapping ("assistants")
@RestController
public class LaboratoryAssistantEndpoint {

    private final LaboratoryAssistantRepository laboratoryAssistantRepository;

    LaboratoryAssistantEndpoint(LaboratoryAssistantRepository laboratoryAssistantRepository) {
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
    }

    @IsAdministrator @IsAssistant
    @GetMapping ("/{id}")
    public LaboratoryAssistant getLaboratoryAssistant(@PathVariable Long id){
        return laboratoryAssistantRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("LaboratoryAssistant","id",id.toString()));
    }

    @IsAdministrator
    @GetMapping
    public List<LaboratoryAssistant> getAllLaboratoryAssistants(){
        return laboratoryAssistantRepository.findAll();
    }

    @IsAdministrator
    @PostMapping
    public LaboratoryAssistant addLaboratoryAssistant(@RequestBody LaboratoryAssistant newLaboratoryAssistant){
        return laboratoryAssistantRepository.save(newLaboratoryAssistant);
    }
}
