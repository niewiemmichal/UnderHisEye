package pl.niewiemmichal.underhiseye.web.endpoints;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.entities.Patient;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"patient"})
@RestController
@RequestMapping(value = "patient", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientEndpoint {

    private final PatientRepository patientRepository;

    @Autowired
    PatientEndpoint(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @ApiOperation(value = "Get patient's details by id")
    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping(value = "/{id}")
    public Patient getPatient(@ApiParam(value = "Patient's id", required = true) @PathVariable Long id){
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Patient", "id", id.toString()));
    }

    @ApiOperation(value = "Get all patients")
    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping
    public List<Patient> getAllPatients(){
        return  patientRepository.findAll();
    }

    @ApiOperation(value = "Register patient")
    @RolesAllowed({"REGISTRANT"})
    @PostMapping
    public Patient patient(
            @ApiParam(value = "New patient's details", required = true)
            @Valid @RequestBody Patient patient){
        return patientRepository.save(patient);
    }
}
