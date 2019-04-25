package pl.niewiemmichal.underhiseye.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.niewiemmichal.underhiseye.commons.dto.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;
import pl.niewiemmichal.underhiseye.repositories.VisitRepository;

public class DefaultVisitServiceTest
{

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private RegistrantRepository registrantRepository;
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private ExaminationService examinationService;

    @InjectMocks
    private DefaultVisitService visitService;

    private final Doctor DOCTOR = new Doctor("Existing", "Doctor", "123");
    private final Address PATIENT_ADRESS = new Address("City", "Street", "HN");

    private final Patient PATIENT =
            new Patient("Existing", "Patient", "123", PATIENT_ADRESS);

    private final Registrant REGISTRANT =
            new Registrant("Existing", "Registrator");

    private final Visit VISIT =
            new Visit("description", VisitStatus.REGISTERED, LocalDate.of(2019, 12, 20), PATIENT, REGISTRANT, DOCTOR);

    private final VisitRegistrationDto VISIT_REGISTRATION_DTO = new VisitRegistrationDto();
    private final VisitClosureDto VISIT_CLOSURE_DTO = new VisitClosureDto();

    private static Long NOT_EXISTING_DOCTOR_ID = 10L;
    private static Long NOT_EXISTING_PATIENT_ID = 20L;
    private static Long NOT_EXISTING_REGISTRANT_ID = 30L;
    private static Long NOT_EXISTING_VISIT_ID = 40L;

    @Before
    public void setUpMocks() {
        initMocks(this);
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

        VISIT_REGISTRATION_DTO.setDoctorId(DOCTOR.getId());
        VISIT_REGISTRATION_DTO.setPatientId(PATIENT.getId());
        VISIT_REGISTRATION_DTO.setRegistrantId(REGISTRANT.getId());
        VISIT_REGISTRATION_DTO.setDate(VISIT.getDate());

        VISIT_CLOSURE_DTO.setDescription(VISIT.getDescription());
    }

    @Test
    public void shouldRegisterVisit() {
        //when
        Visit created = visitService.register(VISIT_REGISTRATION_DTO);

        //then
        assertThat(created).isEqualTo(VISIT);
        verify(visitRepository).save(VISIT);
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfDoctorDoesNotExist() {
        //given
        VISIT.getDoctor().setId(NOT_EXISTING_DOCTOR_ID);

        //when
        Visit created = visitService.register(VISIT_REGISTRATION_DTO);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfRegistrantDoesNotExist() {
        //given
        VISIT.getRegistrationSpecialist().setId(NOT_EXISTING_REGISTRANT_ID);

        //when
        Visit created = visitService.register(VISIT_REGISTRATION_DTO);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfPatientDoesNotExist() {
        //given
        VISIT.getPatient().setId(NOT_EXISTING_PATIENT_ID);

        //when
        Visit created = visitService.register(VISIT_REGISTRATION_DTO);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotCreateVisitIfPastDate() {
        //given
        VISIT.setDate(LocalDate.of(1997, 9, 25));

        visitService.register(VISIT_REGISTRATION_DTO);

        //then
        //expect exception
    }

    @Test
    public void shouldCreatePatient() {
        //given
        VISIT.getPatient().setId(null);
        given(patientRepository.save(PATIENT)).willReturn(PATIENT);

        //when
        Visit created = visitService.register(VISIT_REGISTRATION_DTO);

        //then
        assertThat(created).isEqualTo(VISIT);
        verify(visitRepository).save(VISIT);
        verify(patientRepository).save(PATIENT);
    }

    @Test
    public void shouldCancelVisit() {
        //given
        VISIT.setDescription(null);
        //when
        visitService.cancel(VISIT.getId(), "reason");

        //then
        assertThat(VISIT.getStatus()).isEqualTo(VisitStatus.CANCELED);
        assertThat(VISIT.getDescription()).isEqualTo("reason");
        verify(visitRepository).save(VISIT);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToCancelNonExistingVisit() {
        //given
        //when
        visitService.cancel(NOT_EXISTING_VISIT_ID, "reason");

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotCancelVisitIfFinished() {
        //given
        VISIT.setStatus(VisitStatus.FINISHED);

        //when
        visitService.cancel(VISIT.getId(), "reason");

        //then
        //expect exception
    }

    @Test
    public void shouldEndVisit() {
        //given
        //when
        visitService.end(VISIT.getId(), VISIT_CLOSURE_DTO);
        //then
        assertThat(VISIT.getStatus()).isEqualTo(VisitStatus.FINISHED);
        assertThat(VISIT.getDescription()).isEqualTo(VISIT_CLOSURE_DTO.getDescription());
    }

    @Test
    public void shouldSetDiagnosisIfPresent() {
        //given
        VISIT_CLOSURE_DTO.setDiagnosis("diagnosis");
        //when
        visitService.end(VISIT.getId(), VISIT_CLOSURE_DTO);
        //then
        assertThat(VISIT.getDiagnosis()).isEqualTo(VISIT_CLOSURE_DTO.getDiagnosis());
    }

    @Test
    public void shouldCreatePhysicalExaminationsIfPresent() {
        //given
        final List<PhysicalExaminationDto> examinationList = Lists.newArrayList(new PhysicalExaminationDto(),
                new PhysicalExaminationDto());
        VISIT_CLOSURE_DTO.setPhysicalExaminations(examinationList);
        //when
        visitService.end(VISIT.getId(), VISIT_CLOSURE_DTO);
        //then
        verify(examinationService).createPhysicalExaminations(examinationList);
    }

    @Test
    public void shouldCreateLaboratoryExaminationsIfPresent() {
        //given
        final List<LaboratoryExaminationDto> examinationList= Lists.newArrayList(new LaboratoryExaminationDto(),
                new LaboratoryExaminationDto());
        VISIT_CLOSURE_DTO.setLaboratoryExaminations(examinationList);
        //when
        visitService.end(VISIT.getId(), VISIT_CLOSURE_DTO);
        //then
        verify(examinationService).createLaboratoryExaminations(examinationList);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToEndNonExistingVisit() {
        //given
        //when
        visitService.end(NOT_EXISTING_VISIT_ID, VISIT_CLOSURE_DTO);
        //then
    }

    @Test
    public void shouldGetVisit() {
        //given
        //when
        Visit actual = visitService.get(VISIT.getId());
        //then
        assertThat(actual).isEqualTo(VISIT);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToGetNonExistingVisit() {
        //given
        //when
        visitService.get(NOT_EXISTING_VISIT_ID);
        //then
        //expect exception
    }

    @Test
    public void shouldGetFatVisit() {
        //given
        given(examinationService.getAllLaboratoryExaminationsByVisit(VISIT.getId())).willReturn(Lists.newArrayList(
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), VISIT),
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), VISIT),
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), VISIT)
        ));
        given(examinationService.getAllPhysicalExaminationsByVisit(VISIT.getId())).willReturn(Lists.newArrayList(
                new PhysicalExamination("result", new Examination("name", "code"), VISIT),
                new PhysicalExamination("result", new Examination("name", "code"), VISIT),
                new PhysicalExamination("result", new Examination("name", "code"), VISIT)
        ));
        //when
        VisitWithExaminationsDto actual = visitService.getFatVisit(VISIT.getId());

        //then
        assertThat(actual.getLaboratoryExaminations().size()).isEqualTo(3);
        assertThat(actual.getPhysicalExaminations().size()).isEqualTo(3);
        assertThat(actual.getVisit()).isEqualTo(VISIT);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToGetNonExistingFatVisit() {
        //given
        //when
        visitService.getFatVisit(NOT_EXISTING_VISIT_ID);
        //then
        //expect exception
    }

    @Test
    public void shouldGetAllVisits() {
        //given
        List<Visit> visits = Lists.newArrayList(
                new Visit("1", VisitStatus.REGISTERED, LocalDate.now(), PATIENT, REGISTRANT, DOCTOR),
                new Visit("2", VisitStatus.CANCELED, LocalDate.now(), PATIENT, REGISTRANT, DOCTOR),
                new Visit("3", VisitStatus.FINISHED, LocalDate.now(), PATIENT, REGISTRANT, DOCTOR)
        );
        given(visitRepository.findAll()).willReturn(visits);
        //when
        List<Visit> actual = visitService.getAll();
        //then
        assertThat(actual).containsExactlyElementsOf(visits);
    }

    @Test
    public void shouldGetNoVisits() {
        //given
        given(visitRepository.findAll()).willReturn(Lists.newArrayList());
        //when
        List<Visit> actual = visitService.getAll();
        //then
        assertThat(actual).isEmpty();
    }

}