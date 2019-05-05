package pl.niewiemmichal.underhiseye.web.endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.entities.Doctor;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("doctors")
public class DoctorEndpoint {

    private final DoctorRepository doctorRepository;
    private final RegistrationService registrationService;

    @Autowired
    DoctorEndpoint(DoctorRepository doctorRepository, RegistrationService registrationService) {
        this.doctorRepository = doctorRepository;
        this.registrationService = registrationService;
    }

    @RolesAllowed({"DOCTOR", "ADMINISTRATOR"})
    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id){
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Doctor", "id", id.toString()));
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<Doctor> getAllDoctors(){
        return  doctorRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public Doctor addDoctor(@Valid @RequestBody NewUserDto newDoctor){
        return registrationService.registerDoctor(newDoctor);
    }
}
