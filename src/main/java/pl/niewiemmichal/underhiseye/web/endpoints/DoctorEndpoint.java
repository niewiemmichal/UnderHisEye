package pl.niewiemmichal.underhiseye.web.endpoints;
import org.springframework.http.ResponseEntity;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.model.Doctor;
import pl.niewiemmichal.underhiseye.repository.DoctorRepository;

import java.util.List;

@RequestMapping("doctors")
@RestController
public class DoctorEndpoint {

    private final DoctorRepository doctorRepository;

    DoctorEndpoint(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id){
        return doctorRepository.findById(id).orElseThrow(() ->
                new ResourceDoesNotExistException("Doctor","id",id.toString()));
    }

    @GetMapping
    public List<Doctor> getAllDoctors(){
        return  doctorRepository.findAll();
    }

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor newDoctor){
        if(newDoctor.getId() != null) throw new BadRequestException();
        return doctorRepository.save(newDoctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id){
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(newDoctor.getName());
                    doctor.setSurname(newDoctor.getSurname());
                    doctor.setGmcNumber(newDoctor.getGmcNumber());
                    return doctorRepository.save(doctor);
                })
                .orElseGet(() -> {
                    newDoctor.setId(id);
                    return doctorRepository.save(newDoctor);
                });
    }
}
