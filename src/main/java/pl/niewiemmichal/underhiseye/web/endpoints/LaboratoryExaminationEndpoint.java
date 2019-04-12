package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.repository.LaboratoryExaminationRepository;

import java.util.List;

@RequestMapping ("/laboratoryexaminations")
@RestController
public class LaboratoryExaminationEndpoint {

    private final LaboratoryExaminationRepository laboratoryExaminationRepository;

    LaboratoryExaminationEndpoint(LaboratoryExaminationRepository laboratoryExaminationRepository) {
        this.laboratoryExaminationRepository = laboratoryExaminationRepository;
    }

    @GetMapping ("/{id}")
    public LaboratoryExamination getLaboratoryExamination(@PathVariable Long id){
        return laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("LaboratoryExamination","id",id.toString()));
    }

    @GetMapping
    public List<LaboratoryExamination> getAllLaboratoryExaminations(){
        return (List<LaboratoryExamination>) laboratoryExaminationRepository.findAll();
    }

    @PostMapping
    public LaboratoryExamination addLaboratoryExamination(@RequestBody LaboratoryExamination newLaboratoryExamination){
        return laboratoryExaminationRepository.save(newLaboratoryExamination);
    }

    @PutMapping("/{id}")
    public LaboratoryExamination updateLaboratoryExamination(@RequestBody LaboratoryExamination newLaboratoryExamination,
                                                             @PathVariable Long id) {
        if(!laboratoryExaminationRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("LaboratoryExamination", "id", id.toString());
        else if(newLaboratoryExamination.getId() != null && !(id.equals(newLaboratoryExamination.getId())))
            throw new ResourceConflictException("LaboratoryExamination", "id", id.toString(), newLaboratoryExamination.getId().toString());
        else {
            newLaboratoryExamination.setId(id);
            return laboratoryExaminationRepository.save(newLaboratoryExamination);
        }
    }
}
