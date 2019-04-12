package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.PhysicalExamination;
import pl.niewiemmichal.underhiseye.repository.PhysicalExaminationRepository;

import java.util.List;

@RequestMapping ("/physicalexaminations")
@RestController
public class PhysicalExaminationEndpoint {

    private final PhysicalExaminationRepository physicalExaminationRepository;

    PhysicalExaminationEndpoint(PhysicalExaminationRepository physicalExaminationRepository) {
        this.physicalExaminationRepository = physicalExaminationRepository;
    }

    @GetMapping ("/{id}")
    public PhysicalExamination getPhysicalExamination(@PathVariable Long id){
        return physicalExaminationRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("PhysicalExamination","id",id.toString()));
    }

    @GetMapping
    public List<PhysicalExamination> getAllPhysicalExaminations(){
        return physicalExaminationRepository.findAll();
    }

    @PostMapping
    public PhysicalExamination addPhysicalExamination(@RequestBody PhysicalExamination newPhysicalExamination){
        return physicalExaminationRepository.save(newPhysicalExamination);
    }

    @PutMapping("/{id}")
    public PhysicalExamination updatePhysicalExamination(@RequestBody PhysicalExamination newPhysicalExamination, @PathVariable Long id) {
        if(!physicalExaminationRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("PhysicalExamination", "id", id.toString());
        else if(newPhysicalExamination.getId() != null && !(id.equals(newPhysicalExamination.getId())))
            throw new ResourceConflictException("PhysicalExamination", "id", id.toString(), newPhysicalExamination.getId().toString());
        else {
            newPhysicalExamination.setId(id);
            return physicalExaminationRepository.save(newPhysicalExamination);
        }
    }
}

