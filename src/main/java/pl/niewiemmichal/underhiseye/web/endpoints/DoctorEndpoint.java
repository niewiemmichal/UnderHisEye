package pl.niewiemmichal.underhiseye.web.endpoints;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAdministrator;
import pl.niewiemmichal.underhiseye.commons.annotations.IsDoctor;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.entities.Doctor;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;

import java.util.List;

@RequestMapping("doctors")
@RestController
public class DoctorEndpoint {

    private final DoctorRepository doctorRepository;

    DoctorEndpoint(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @IsAdministrator @IsDoctor
    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id){
        return doctorRepository.findById(id).orElseThrow(() ->
                new ResourceDoesNotExistException("Doctor", "id", id.toString()));
    }

    @IsAdministrator
    @GetMapping
    public List<Doctor> getAllDoctors(){
        return  doctorRepository.findAll();
    }

    @IsAdministrator
    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor newDoctor){
        if(newDoctor.getId() != null) throw new BadRequestException();
        return doctorRepository.save(newDoctor);
    }
}
