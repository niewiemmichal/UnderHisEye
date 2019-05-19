package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"registrants"})
@RestController
@RequestMapping(value = "registrants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrantEndpoint {

    private final RegistrantRepository registrantRepository;
    private final RegistrationService registrationService;

    RegistrantEndpoint(RegistrantRepository registrantRepository, RegistrationService registrationService) {
        this.registrantRepository = registrantRepository;
        this.registrationService = registrationService;
    }

    @ApiOperation("Get registrant's details by id")
    @RolesAllowed({"REGISTRANT", "ADMINISTRATOR"})
    @GetMapping("/{id}")
    public Registrant getPatientRegistrationSpecialist(
            @ApiParam(value = "Registrant's id", required = true) @PathVariable Long id)
    {
        return registrantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("PatientRegistrationSpecialistEndpoint", "id",
                        id.toString()));
    }

    @ApiOperation("Get registrant's details by its user's username")
    @RolesAllowed({"REGISTRANT", "ADMINISTRATOR"})
    @GetMapping("/u/{username}")
    public Registrant getPatientRegistrationSpecialist(
            @ApiParam(value = "Registrant's username", required = true) @PathVariable String username)
    {
        return registrantRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceDoesNotExistException("RegistrantEndpoint", "username", username));
    }

    @ApiOperation("Get all registrants")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<Registrant> getAllPatientRegistrationSpecialists(){
        return registrantRepository.findAll();
    }

    @ApiOperation("Add registrant")
    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public Registrant addPatientRegistrationSpecialist(
            @ApiParam(value = "New registrant's details", required = true) @Valid @RequestBody NewUserDto newRegistrant)
    {
        return registrationService.registerRegistrant(newRegistrant);
    }
}
