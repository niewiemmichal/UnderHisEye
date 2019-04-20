package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.model.Visit;
import pl.niewiemmichal.underhiseye.repository.DoctorRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRegistrationSpecialistRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRepository;

@Service
public class DefaultVisitService implements VisitService
{

    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private PatientRegistrationSpecialistRepository registratorRepository;

    @Autowired
    public DefaultVisitService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
                               final PatientRegistrationSpecialistRepository registratorRepository)
    {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registratorRepository = registratorRepository;
    }

    public void cancel(Long visitId) {}
    public Visit register(Visit visit) { return null; }
}
