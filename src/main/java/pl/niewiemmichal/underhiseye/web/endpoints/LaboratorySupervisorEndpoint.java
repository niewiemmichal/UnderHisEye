package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.LaboratorySupervisor;
import pl.niewiemmichal.underhiseye.repositories.LaboratorySupervisorRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"supervisors"})
@RestController
@RequestMapping(value = "supervisors", produces = MediaType.APPLICATION_JSON_VALUE)
public class LaboratorySupervisorEndpoint {

    private final LaboratorySupervisorRepository laboratorySupervisorRepository;
    private final RegistrationService registrationService;

    @Autowired
    LaboratorySupervisorEndpoint(LaboratorySupervisorRepository laboratorySupervisorRepository,
                                 RegistrationService registrationService) {
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.registrationService = registrationService;
    }

    @ApiOperation("Get laboratory supervisor's details by id")
    @RolesAllowed({"REGISTRANT", "ADMINISTRATOR"})
    @GetMapping("/{id}")
    public LaboratorySupervisor getLaboratorySupervisor(
            @ApiParam(value = "Laboratory supervisor's id", required = true) @PathVariable Long id)
    {
        return laboratorySupervisorRepository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException("LaboratorySupervisor","id",id.toString()));
    }

    @ApiOperation("Get laboratory supervisor's details by its user's username")
    @RolesAllowed({"REGISTRANT"})
    @GetMapping("/{username}")
    public LaboratorySupervisor getLaboratorySupervisor(
            @ApiParam(value = "Laboratory supervisor's username", required = true) @PathVariable String username)
    {
        return laboratorySupervisorRepository.findByUser_Username(username).orElseThrow(
                () -> new ResourceDoesNotExistException("LaboratorySupervisor","username",username));
    }

    @ApiOperation("Get all laboratory supervisors")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<LaboratorySupervisor> getAllLaboratorySupervisors(){
        return laboratorySupervisorRepository.findAll();
    }

    @ApiOperation("Add laboratory supervisor")
    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public LaboratorySupervisor addLaboratorySupervisor(
            @ApiParam(value = "New laboratory supervisor's details", required = true)
            @Valid @RequestBody NewUserDto newSupervisor){
        return registrationService.registerSupervisor(newSupervisor);
    }
}