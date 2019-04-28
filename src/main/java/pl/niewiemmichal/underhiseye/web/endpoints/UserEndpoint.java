package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAdministrator;
import pl.niewiemmichal.underhiseye.commons.annotations.IsStaff;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Role;
import pl.niewiemmichal.underhiseye.entities.User;
import pl.niewiemmichal.underhiseye.repositories.UserRepository;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserEndpoint {

    private final UserRepository userRepository;

    @Autowired
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @IsStaff
    @GetMapping("/{username}")
    public User getUserDetails(@PathVariable String username){
        return userRepository.findById(username).orElseThrow(
                () -> new ResourceDoesNotExistException("User", "username", username));
    }

    @IsAdministrator
    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @IsAdministrator
    @PostMapping
    public User addAdministrator(@RequestBody User user){
        if(!user.getRoles().contains(Role.ADMINISTRATOR)) user.addRole(Role.ADMINISTRATOR);
        return userRepository.save(user);
    }

}
