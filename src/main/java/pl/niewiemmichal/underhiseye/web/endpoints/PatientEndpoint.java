package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.Patient;
import pl.niewiemmichal.underhiseye.repository.PatientRepository;

import java.util.List;

@RequestMapping ("/patients")
@RestController
public class PatientEndpoint {

    private final PatientRepository patientRepository;

    PatientEndpoint(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping ("/{id}")
    public Patient getPatient(@PathVariable Long id){
        return patientRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("Patient","id",id.toString()));
    }

    @GetMapping
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient newPatient){
        return patientRepository.save(newPatient);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@RequestBody Patient newPatient, @PathVariable Long id) {
        if(!patientRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Patient", "id", id.toString());
        else if(newPatient.getId() != null && !(id.equals(newPatient.getId())))
            throw new ResourceConflictException("Patient", "id", id.toString(), newPatient.getId().toString());
        else {
            newPatient.setId(id);
            return patientRepository.save(newPatient);
        }
    }

}
