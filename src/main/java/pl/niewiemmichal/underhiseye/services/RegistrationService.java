package pl.niewiemmichal.underhiseye.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import pl.niewiemmichal.underhiseye.model.Patient;
import pl.niewiemmichal.underhiseye.model.Visit;
import pl.niewiemmichal.underhiseye.repository.DoctorRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRegistrationSpecialistRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRepository;

public class RegistrationService
{

    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private PatientRegistrationSpecialistRepository registratorRepository;

    @Autowired
    public RegistrationService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
            final PatientRegistrationSpecialistRepository registratorRepository)
    {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registratorRepository = registratorRepository;
    }

    void cancel(Integer visitId) {}
    Visit register(Integer patientId, Integer doctorId, Integer registrantId, Date date) { return null; }
    Visit register(Patient patient, Integer doctorId, Integer registrantId, Date date) { return null; }
}
