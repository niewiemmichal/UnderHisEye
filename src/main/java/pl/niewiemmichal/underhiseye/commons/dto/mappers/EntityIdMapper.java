package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Doctor;
import pl.niewiemmichal.underhiseye.entities.Patient;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;

@Component
public class EntityIdMapper {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private RegistrantRepository registrantRepository;

    @Autowired
    public EntityIdMapper(PatientRepository patientRepository,
                          DoctorRepository doctorRepository,
                          RegistrantRepository registrantRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.registrantRepository = registrantRepository;
    }

    Patient toPatient(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Patient", "id", id.toString()));
    }

    Doctor toDoctor(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Doctor", "id", id.toString()));
    }

    Registrant toRegistrant(Long id) {
        return registrantRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Registrant", "id", id.toString()));
    }

}
