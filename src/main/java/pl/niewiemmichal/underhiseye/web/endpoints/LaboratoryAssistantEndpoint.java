package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.entities.LaboratoryAssistant;
import pl.niewiemmichal.underhiseye.repositories.LaboratoryAssistantRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"assistants"})
@RestController
@RequestMapping (value = "assistants", produces = MediaType.APPLICATION_JSON_VALUE)
public class LaboratoryAssistantEndpoint {

    private final LaboratoryAssistantRepository laboratoryAssistantRepository;
    private final RegistrationService registrationService;

    @Autowired
    LaboratoryAssistantEndpoint(LaboratoryAssistantRepository laboratoryAssistantRepository,
                                RegistrationService registrationService) {
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
        this.registrationService = registrationService;
    }

    @ApiOperation("Get laboratory assistant's details by id")
    @RolesAllowed({"ASSISTANT", "ADMINISTRATOR"})
    @GetMapping ("/{id}")
    public LaboratoryAssistant getLaboratoryAssistant(
            @ApiParam(value = "Laboratory assistant's id", required = true) @PathVariable Long id)
    {
        return laboratoryAssistantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("LaboratoryAssistant", "id", id.toString()));
    }

    @ApiOperation("Get all laboratory assistants")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<LaboratoryAssistant> getAllLaboratoryAssistants(){
        return laboratoryAssistantRepository.findAll();
    }

    @ApiOperation("Add laboratory assistant")
    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public LaboratoryAssistant addLaboratoryAssistant(
            @ApiParam(value = "New laboratory assistant's details", required = true)
            @Valid @RequestBody NewUserDto newAssistant)
    {
        return registrationService.registerAssistant(newAssistant);
    }
}
