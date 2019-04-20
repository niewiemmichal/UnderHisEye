package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.PatientRegistrationSpecialist;
import pl.niewiemmichal.underhiseye.repository.PatientRegistrationSpecialistRepository;

import java.util.List;

@Controller
@RequestMapping("patientRegistrationSpecialist")
@ResponseBody
public class PatientRegistrationSpecialistEndpoint {

    private final PatientRegistrationSpecialistRepository patientRegistrationSpecialistRepository;

    PatientRegistrationSpecialistEndpoint(PatientRegistrationSpecialistRepository patientRegistrationSpecialistRepository) {
        this.patientRegistrationSpecialistRepository = patientRegistrationSpecialistRepository;
    }

    @GetMapping("/{id}")
    public PatientRegistrationSpecialist getPatientRegistrationSpecialist(@PathVariable Long id){
        return patientRegistrationSpecialistRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("PatientRegistrationSpecialistEndpoint","id",id.toString()));
    }

    @GetMapping
    public List<PatientRegistrationSpecialist> getAllPatientRegistrationSpecialists(){
        return (List<PatientRegistrationSpecialist>) patientRegistrationSpecialistRepository.findAll();
    }

    @PostMapping
    public PatientRegistrationSpecialist addPatientRegistrationSpecialist(@RequestBody PatientRegistrationSpecialist newPatientRegistrationSpecialist){
        return patientRegistrationSpecialistRepository.save(newPatientRegistrationSpecialist);
    }

    @PutMapping("/{id}")
    public PatientRegistrationSpecialist updatePatientRegistrationSpecialist(@RequestBody PatientRegistrationSpecialist newPatientRegistrationSpecialist, @PathVariable Long id){
        if(!patientRegistrationSpecialistRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("PatientRegistrationSpecialist", "id", id.toString());
        else if(newPatientRegistrationSpecialist.getId() != null && !(id.equals(newPatientRegistrationSpecialist.getId())))
            throw new ResourceConflictException("PatientRegistrationSpecialist", "id", id.toString(), newPatientRegistrationSpecialist.getId().toString());
        else {
            newPatientRegistrationSpecialist.setId(id);
            return patientRegistrationSpecialistRepository.save(newPatientRegistrationSpecialist);
        }
    }

}
