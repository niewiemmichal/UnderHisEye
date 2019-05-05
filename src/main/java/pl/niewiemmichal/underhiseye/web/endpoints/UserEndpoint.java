package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("users")
public class UserEndpoint {

    private final UserRepository userRepository;
    private final RegistrationService registrationService;

    @Autowired
    public UserEndpoint(UserRepository userRepository, RegistrationService registrationService) {
        this.userRepository = userRepository;
        this.registrationService = registrationService;
    }

    @RolesAllowed({"ADMINISTRATOR", "DOCTOR", "REGISTRANT", "SUPERVISOR", "ASSISTANT"})
    @GetMapping("/{username}")
    public User getUserDetails(@PathVariable String username){
        return userRepository.findById(username)
                .orElseThrow(() -> new ResourceDoesNotExistException("User", "username", username));
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PostMapping
    public User addPatientRegistrationSpecialist(@Valid @RequestBody NewUserDto newUserDto){
        return registrationService.registerAdministrator(newUserDto);
    }
}
