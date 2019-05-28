package pl.niewiemmichal.underhiseye.web.endpoints;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.entities.Doctor;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"doctors"})
@RestController
@RequestMapping(value = "doctors", produces = MediaType.APPLICATION_JSON_VALUE)
public class DoctorEndpoint {

    private final DoctorRepository doctorRepository;
    private final RegistrationService registrationService;

    @Autowired
    DoctorEndpoint(DoctorRepository doctorRepository, RegistrationService registrationService) {
        this.doctorRepository = doctorRepository;
        this.registrationService = registrationService;
    }

    @ApiOperation(value = "Get doctor's details by id")
    @RolesAllowed({"DOCTOR", "REGISTRANT", "ADMINISTRATOR"})
    @GetMapping(value = "/{id}")
    public Doctor getDoctor(@ApiParam(value = "Doctor's id", required = true) @PathVariable Long id){
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Doctor", "id", id.toString()));
    }

    @ApiOperation(value = "Get doctor's details by its user's username")
    @RolesAllowed({"DOCTOR", "REGISTRANT", "ADMINISTRATOR"})
    @GetMapping(value = "/u/{username}")
    public Doctor getDoctor(@ApiParam(value = "Doctor's username", required = true) @PathVariable String username){
        return doctorRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceDoesNotExistException("Doctor", "username", username));
    }

    @ApiOperation(value = "Get all doctors")
    @RolesAllowed({"ADMINISTRATOR", "REGISTRANT"})
    @GetMapping
    public List<Doctor> getAllDoctors(){
        return  doctorRepository.findAll();
    }

    @ApiOperation(value = "Add doctor")
    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public Doctor addDoctor(
            @ApiParam(value = "New doctor's details", required = true)
            @Valid @RequestBody NewUserDto newDoctor){
        return registrationService.registerDoctor(newDoctor);
    }
}
