package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.entities.User;
import pl.niewiemmichal.underhiseye.repositories.UserRepository;
import pl.niewiemmichal.underhiseye.services.RegistrationService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"users"})
@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserEndpoint {

    private final UserRepository userRepository;
    private final RegistrationService registrationService;

    @Autowired
    public UserEndpoint(UserRepository userRepository, RegistrationService registrationService) {
        this.userRepository = userRepository;
        this.registrationService = registrationService;
    }

    @ApiOperation("Get user's details by username")
    @RolesAllowed({"ADMINISTRATOR", "DOCTOR", "REGISTRANT", "SUPERVISOR", "ASSISTANT"})
    @GetMapping("/{username}")
    public User getUserDetails(@ApiParam(value = "User's username", required = true) @PathVariable String username){
        return userRepository.findById(username)
                .orElseThrow(() -> new ResourceDoesNotExistException("User", "username", username));
    }

    @ApiOperation("Get all users")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @ApiOperation("Add administrator")
    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public User addPatientRegistrationSpecialist(
            @ApiParam(value = "New administrator's details", required = true)
            @Valid @RequestBody NewUserDto newAdministrator) {
        return registrationService.registerAdministrator(newAdministrator);
    }
}
