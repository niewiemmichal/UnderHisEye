package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;

@Service
public class DefaultVisitService implements VisitService
{

    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private RegistrantRepository registratorRepository;

    @Autowired
    public DefaultVisitService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
                               final RegistrantRepository registratorRepository)
    {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registratorRepository = registratorRepository;
    }

    public void cancel(Long visitId) {}
    public Visit register(Visit visit) { return null; }
}
