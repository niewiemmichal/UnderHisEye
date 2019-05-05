package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.NewUserDto;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.LaboratoryAssistantRepository;
import pl.niewiemmichal.underhiseye.repositories.LaboratorySupervisorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;

import javax.print.Doc;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class RegistrationService {

    private final UserService userService;
    private final LaboratoryAssistantRepository assistantRepository;
    private final LaboratorySupervisorRepository supervisorRepository;
    private final RegistrantRepository registrantRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public RegistrationService(UserService userService,
                               LaboratoryAssistantRepository assistantRepository,
                               LaboratorySupervisorRepository supervisorRepository,
                               RegistrantRepository registrantRepository,
                               DoctorRepository doctorRepository) {
        this.userService = userService;
        this.assistantRepository = assistantRepository;
        this.supervisorRepository = supervisorRepository;
        this.registrantRepository = registrantRepository;
        this.doctorRepository = doctorRepository;
    }

    public Doctor registerDoctor(NewUserDto dto) {
        final User user = userService.create(dto.getUsername(), dto.getPassword(), Role.DOCTOR);
        final Doctor doctor = new Doctor(dto.getFirstName(), dto.getLastName(), dto.getGmcNumber());
        doctor.setUser(user);
        return doctorRepository.save(doctor);
    }

    public Registrant registerRegistrant(NewUserDto dto) {
        final User user = userService.create(dto.getUsername(), dto.getPassword(), Role.REGISTRANT);
        final Registrant registrant = new Registrant(dto.getFirstName(), dto.getLastName());
        registrant.setUser(user);
        return registrantRepository.save(registrant);
    }

    public LaboratoryAssistant registerAssistant(NewUserDto dto) {
        final User user = userService.create(dto.getUsername(), dto.getPassword(), Role.ASSISTANT);
        final LaboratoryAssistant assistant = new LaboratoryAssistant(dto.getFirstName(), dto.getLastName());
        assistant.setUser(user);
        return assistantRepository.save(assistant);
    }

    public LaboratorySupervisor registerSupervisor(NewUserDto dto) {
        final User user = userService.create(dto.getUsername(), dto.getPassword(), Role.SUPERVISOR);
        final LaboratorySupervisor supervisor = new LaboratorySupervisor(dto.getFirstName(), dto.getLastName());
        supervisor.setUser(user);
        return supervisorRepository.save(supervisor);
    }

    public User registerAdministrator(NewUserDto dto) {
        return userService.create(dto.getUsername(), dto.getPassword(), Role.ADMINISTRATOR);
    }
}
