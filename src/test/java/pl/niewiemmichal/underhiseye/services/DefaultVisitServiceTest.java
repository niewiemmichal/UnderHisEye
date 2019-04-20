package pl.niewiemmichal.underhiseye.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.niewiemmichal.underhiseye.commons.exceptions.VisitServiceException;
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

public class DefaultVisitServiceTest
{

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRegistrationSpecialistRepository registrantRepository;
    @Mock
    private VisitRepository visitRepository;
    @InjectMocks
    private DefaultVisitService visitService;

    private static final Doctor DOCTOR = new Doctor("Existing", "Doctor", "123");
    private static final Address PATIENT_ADRESS = new Address("City", "Street", "HN");

    private static final Patient PATIENT =
            new Patient("Existing", "Patient", "123", PATIENT_ADRESS);

    private static final PatientRegistrationSpecialist REGISTRANT =
            new PatientRegistrationSpecialist("Existing", "Registrator");

    private static final Visit VISIT =
            new Visit("description", LocalDate.of(2019, 12, 20), PATIENT, REGISTRANT, DOCTOR);

    private static Long NOT_EXISTING_DOCTOR_ID = 10L;
    private static Long NOT_EXISTING_PATIENT_ID = 20L;
    private static Long NOT_EXISTING_REGISTRANT_ID = 30L;
    private static Long NOT_EXISTING_VISIT_ID = 40L;

    @Before
    public void setUpMocks() {
        DOCTOR.setId(1L);
        PATIENT.setId(2L);
        REGISTRANT.setId(3L);

        given(doctorRepository.findById(DOCTOR.getId())).willReturn(Optional.of(DOCTOR));
        given(patientRepository.findById(PATIENT.getId())).willReturn(Optional.of(PATIENT));
        given(registrantRepository.findById(REGISTRANT.getId())).willReturn(Optional.of(REGISTRANT));

        given(doctorRepository.findById(NOT_EXISTING_DOCTOR_ID)).willReturn(Optional.empty());
        given(patientRepository.findById(NOT_EXISTING_PATIENT_ID)).willReturn(Optional.empty());
        given(registrantRepository.findById(NOT_EXISTING_REGISTRANT_ID)).willReturn(Optional.empty());

        VISIT.setId(4L);
        given(visitRepository.findById(VISIT.getId())).willReturn(Optional.of(VISIT));

        given(visitRepository.findById(NOT_EXISTING_VISIT_ID)).willReturn(Optional.empty());

        given(visitRepository.save(VISIT)).willReturn(VISIT);
    }

    @Test
    public void shouldRegisterVisit() {
        //when
        Visit created = visitService.register(VISIT);

        //then
        assertThat(created).isEqualTo(VISIT);
        verify(visitRepository).save(VISIT);
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotRegisterVisitIfDoctorDoesNotExist() {
        //given
        VISIT.getDoctor().setId(NOT_EXISTING_DOCTOR_ID);

        //when
        Visit created = visitService.register(VISIT);

        //then
        //expect exception
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotRegisterVisitIfRegistrantDoesNotExist() {
        //given
        VISIT.getRegistrationSpecialist().setId(NOT_EXISTING_REGISTRANT_ID);

        //when
        Visit created = visitService.register(VISIT);

        //then
        //expect exception
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotRegisterVisitIfPatientDoesNotExist() {
        //given
        VISIT.getPatient().setId(NOT_EXISTING_PATIENT_ID);

        //when
        Visit created = visitService.register(VISIT);

        //then
        //expect exception
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotCreateVisitIfPastDate() {
        //given
        VISIT.setDate(LocalDate.of(1997, 9, 25));

        visitService.register(VISIT);

        //then
        //expect exception
    }

    @Test
    public void shouldCreatePatient() {
        //given
        VISIT.getPatient().setId(null);
        given(patientRepository.save(PATIENT)).willReturn(PATIENT);

        //when
        Visit created = visitService.register(VISIT);

        //then
        assertThat(created).isEqualTo(VISIT);
        verify(visitRepository).save(VISIT);
        verify(patientRepository).save(PATIENT);
    }

    @Test
    public void shouldCancelVisit() {
        //given
        //when
        visitService.cancel(VISIT.getId());

        //then
        assertThat(VISIT.getStatus()).isEqualTo(VisitStatus.CANCELED);
        verify(visitRepository).save(VISIT);
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotCancelIfIdOfNotExistingVisitGiven() {
        //given
        //when
        visitService.cancel(NOT_EXISTING_VISIT_ID);

        //then
        //expect exception
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotCancelVisitIfFinished() {
        //given
        VISIT.setStatus(VisitStatus.FINISHED);

        //when
        visitService.cancel(VISIT.getId());

        //then
        //expect exception
    }

    @Test(expected = VisitServiceException.class)
    public void shouldNotCancelVisitIfNoStatus() {
        //given
        VISIT.setStatus(null);

        //when
        visitService.cancel(VISIT.getId());

        //then
        //expect exception
    }

    @Test
    public void shouldEndVisit() {

    }

}