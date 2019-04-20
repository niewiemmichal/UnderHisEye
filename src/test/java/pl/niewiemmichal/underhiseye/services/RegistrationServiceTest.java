package pl.niewiemmichal.underhiseye.services;

import static org.mockito.BDDMockito.given;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.niewiemmichal.underhiseye.model.Address;
import pl.niewiemmichal.underhiseye.model.Doctor;
import pl.niewiemmichal.underhiseye.model.Patient;
import pl.niewiemmichal.underhiseye.model.PatientRegistrationSpecialist;
import pl.niewiemmichal.underhiseye.model.Visit;
import pl.niewiemmichal.underhiseye.model.VisitStatus;
import pl.niewiemmichal.underhiseye.repository.DoctorRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRegistrationSpecialistRepository;
import pl.niewiemmichal.underhiseye.repository.PatientRepository;
import pl.niewiemmichal.underhiseye.repository.VisitRepository;

public class RegistrationServiceTest
{

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRegistrationSpecialistRepository registratorRepository;
    @Mock
    private VisitRepository visitRepository;

    private RegistrationService registrationService;

    private static final Doctor EXISTING_DOCTOR = new Doctor("Existing", "Doctor", "123");
    private static final Address PATIENT_ADRESS = new Address("City", "Street", "HN");
    private static final Patient EXISTING_PATIENT =
            new Patient("Existing", "Patient", "123", PATIENT_ADRESS);
    private static final PatientRegistrationSpecialist EXISTING_REGISTRATOR =
            new PatientRegistrationSpecialist("Existing", "Registrator");
    private static final Visit VISIT = new Visit(VisitStatus.REGISTERED, new Date(), )

    private static Long NOT_EXISTING_DOCTOR_ID = 10L;
    private static Long NOT_EXISTING_PATIENT_ID = 20L;
    private static Long NOT_EXISTING_REGISTRATOR_ID = 30L;

    @Before
    public void setUpMocks() {
        EXISTING_DOCTOR.setId(1L);
        EXISTING_PATIENT.setId(2L);
        EXISTING_REGISTRATOR.setId(3L);
        given(doctorRepository.findById(EXISTING_DOCTOR.getId())).willReturn(Optional.of(EXISTING_DOCTOR));
        given(patientRepository.findById(EXISTING_PATIENT.getId())).willReturn(Optional.of(EXISTING_PATIENT));
        given(registratorRepository.findById(EXISTING_REGISTRATOR.getId())).willReturn(Optional.of(EXISTING_REGISTRATOR));

        given(doctorRepository.findById(NOT_EXISTING_DOCTOR_ID)).willReturn(Optional.empty());
        given(patientRepository.findById(NOT_EXISTING_PATIENT_ID)).willReturn(Optional.empty());
        given(registratorRepository.findById(NOT_EXISTING_REGISTRATOR_ID)).willReturn(Optional.empty());


    }

    @Test
    public void shouldRegisterVisit()
    {

    }


}