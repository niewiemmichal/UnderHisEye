package pl.niewiemmichal.underhiseye.web.endpoints;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.model.Doctor;
import pl.niewiemmichal.underhiseye.repository.DoctorRepository;

import java.util.List;

@Controller
@RequestMapping("doctors")
@ResponseBody
public class DoctorEndpoint {

    private final DoctorRepository doctorRepository;

    DoctorEndpoint(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id){
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Doctor","id",id.toString()));
    }

    @GetMapping
    public List<Doctor> getAllDoctors(){
        return (List<Doctor>) doctorRepository.findAll();
    }

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor newDoctor){
        return doctorRepository.save(newDoctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id){
        if(!doctorRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Question", "id", id.toString());
        else if(newDoctor.getId() != null && !(id.equals(newDoctor.getId())))
            throw new ResourceConflictException("Question", "id", id.toString(), newDoctor.getId().toString());
        else {
            newDoctor.setId(id);
            return doctorRepository.save(newDoctor);
        }
    }
}
