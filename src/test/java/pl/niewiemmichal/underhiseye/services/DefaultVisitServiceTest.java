package pl.niewiemmichal.underhiseye.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.niewiemmichal.underhiseye.commons.dto.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.entities.Registrant;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;
import pl.niewiemmichal.underhiseye.repositories.VisitRepository;

@RunWith(SpringRunner.class)
public class DefaultVisitServiceTest
{

    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private DoctorRepository doctorRepository;
    @MockBean
    private RegistrantRepository registrantRepository;
    @MockBean
    private VisitRepository visitRepository;
    @MockBean
    private ExaminationService examinationService;

    @Autowired
    private DefaultVisitService visitService;

    private Doctor doctor = new Doctor("Existing", "Doctor", "123");
    private Address patientAdress = new Address("City", "Street", "HN");

    private Patient patient = new Patient("Existing", "Patient", "123", patientAdress);

    private Registrant registrant =  new Registrant("Existing", "Registrator");

    private Visit visit = new Visit("description", VisitStatus.REGISTERED,
            LocalDate.of(2019, 12, 20), patient, registrant, doctor);

    private VisitRegistrationDto visitRegistrationDto = new VisitRegistrationDto();
    private VisitClosureDto visitClosureDto = new VisitClosureDto();

    private static final Long NOT_EXISTING_DOCTOR_ID = 10L;
    private static final Long NOT_EXISTING_PATIENT_ID = 20L;
    private static final Long NOT_EXISTING_REGISTRANT_ID = 30L;
    private static final Long NOT_EXISTING_VISIT_ID = 40L;

    @Before
    public void setUpMocks() {
        initMocks(this);
        doctor.setId(1L);
        patient.setId(2L);
        registrant.setId(3L);
        visit.setId(4L);

        given(doctorRepository.findById(doctor.getId())).willReturn(Optional.of(doctor));
        given(patientRepository.findById(patient.getId())).willReturn(Optional.of(patient));
        given(registrantRepository.findById(registrant.getId())).willReturn(Optional.of(registrant));

        given(doctorRepository.findById(NOT_EXISTING_DOCTOR_ID)).willReturn(Optional.empty());
        given(patientRepository.findById(NOT_EXISTING_PATIENT_ID)).willReturn(Optional.empty());
        given(registrantRepository.findById(NOT_EXISTING_REGISTRANT_ID)).willReturn(Optional.empty());

        //visit.setId(4L);
        given(visitRepository.findById(visit.getId())).willReturn(Optional.of(visit));
        given(visitRepository.findById(NOT_EXISTING_VISIT_ID)).willReturn(Optional.empty());
        given(visitRepository.save(visit)).willReturn(visit);

        visitRegistrationDto.setDoctorId(doctor.getId());
        visitRegistrationDto.setPatientId(patient.getId());
        visitRegistrationDto.setRegistrantId(registrant.getId());
        visitRegistrationDto.setDate(visit.getDate());

        visitClosureDto.setDescription(visit.getDescription());
    }

    @Test
    public void shouldRegisterVisit() {
        //when
        Visit created = visitService.register(visitRegistrationDto);

        //then
        assertThat(created).isEqualTo(visit);
        verify(visitRepository).save(visit);
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfDoctorDoesNotExist() {
        //given
        visit.getDoctor().setId(NOT_EXISTING_DOCTOR_ID);
        visitRegistrationDto.setDoctorId(NOT_EXISTING_DOCTOR_ID);

        //when
        visitService.register(visitRegistrationDto);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfRegistrantDoesNotExist() {
        //given
        visit.getRegistrationSpecialist().setId(NOT_EXISTING_REGISTRANT_ID);
        visitRegistrationDto.setRegistrantId(NOT_EXISTING_REGISTRANT_ID);

        //when
        visitService.register(visitRegistrationDto);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotRegisterVisitIfPatientDoesNotExist() {
        //given
        visit.getPatient().setId(NOT_EXISTING_PATIENT_ID);
        visitRegistrationDto.setPatientId(NOT_EXISTING_PATIENT_ID);

        //when
        visitService.register(visitRegistrationDto);

        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotCreateVisitIfPastDate() {
        //given
        visit.setDate(LocalDate.of(1997, 9, 25));
        visitRegistrationDto.setDate(LocalDate.of(1997, 9, 25));

        //when
        visitService.register(visitRegistrationDto);

        //then
        //expect exception
    }

    @Test
    public void shouldCancelVisit() {
        //given
        //when
        visitService.cancel(visit.getId(), "reason");

        //then
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.CANCELED);
        assertThat(visit.getDescription()).isEqualTo("reason");
        verify(visitRepository).save(visit);
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
        visit.setStatus(VisitStatus.FINISHED);

        //when
        visitService.cancel(visit.getId(), "reason");

        //then
        //expect exception
    }

    @Test
    public void shouldNotCancelButPass() {
        //given
        visit.setStatus(VisitStatus.CANCELED);
        visit.setDescription("reason");

        //when
        visitService.cancel(visit.getId(), "anotherReason");

        //then
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.CANCELED);
        assertThat(visit.getDescription()).isEqualTo("reason");
        verify(visitRepository, never()).save(visit);
    }

    @Test
    public void shouldEndVisit() {
        //given
        //when
        visitService.end(visit.getId(), visitClosureDto);
        //then
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.FINISHED);
        assertThat(visit.getDescription()).isEqualTo(visitClosureDto.getDescription());
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotEndVisitIfCanceled() {
        //given
        visit.setStatus(VisitStatus.CANCELED);

        //when
        visitService.end(visit.getId(), visitClosureDto);

        //then
        //expect exception
    }

    @Test
    public void shouldNotEndButPass() {
        //given
        visit.setStatus(VisitStatus.FINISHED);
        visit.setDescription("description");
        visitClosureDto.setDescription("anotherDescription");

        //when
        visitService.end(visit.getId(), visitClosureDto);

        //then
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.FINISHED);
        assertThat(visit.getDescription()).isEqualTo("description");
        verify(visitRepository, never()).save(visit);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToEndNonExistingVisit() {
        //given
        //when
        visitService.end(NOT_EXISTING_VISIT_ID, visitClosureDto);
        //then
    }

    @Test
    public void shouldSetDiagnosisIfPresent() {
        //given
        visitClosureDto.setDiagnosis("diagnosis");
        visit.setId(visit.getId());
        //when
        visitService.end(visit.getId(), visitClosureDto);
        //then
        assertThat(visit.getDiagnosis()).isEqualTo(visitClosureDto.getDiagnosis());
    }

    @Test
    public void shouldCreatePhysicalExaminationsIfPresent() {
        //given
        visit.setId(visit.getId());
        final List<PhysicalExaminationDto> examinationList = Lists.newArrayList(new PhysicalExaminationDto(),
                new PhysicalExaminationDto());
        visitClosureDto.setPhysicalExaminations(examinationList);
        //when
        visitService.end(visit.getId(), visitClosureDto);
        //then
        verify(examinationService).createPhysicalExaminations(examinationList);
    }

    @Test
    public void shouldCreateLaboratoryExaminationsIfPresent() {
        //given
        final List<LaboratoryExaminationDto> examinationList= Lists.newArrayList(new LaboratoryExaminationDto(),
                new LaboratoryExaminationDto());
        visitClosureDto.setLaboratoryExaminations(examinationList);
        //when
        visitService.end(visit.getId(), visitClosureDto);
        //then
        verify(examinationService).createLaboratoryExaminations(examinationList);
    }

    @Test
    public void shouldGetVisit() {
        //given
        visit.setId(visit.getId());
        //when
        Visit actual = visitService.get(visit.getId());
        //then
        assertThat(actual).isEqualTo(visit);
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
        given(examinationService.getAllLaboratoryExaminationsByVisit(visit.getId())).willReturn(Lists.newArrayList(
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), visit),
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), visit),
                new LaboratoryExamination(LaboratoryExamStatus.CANCELED, new Examination("name", "code"), visit)
        ));
        given(examinationService.getAllPhysicalExaminationsByVisit(visit.getId())).willReturn(Lists.newArrayList(
                new PhysicalExamination("result", new Examination("name", "code"), visit),
                new PhysicalExamination("result", new Examination("name", "code"), visit),
                new PhysicalExamination("result", new Examination("name", "code"), visit)
        ));
        //when
        VisitWithExaminationsDto actual = visitService.getFatVisit(visit.getId());

        //then
        assertThat(actual.getLaboratoryExaminations().size()).isEqualTo(3);
        assertThat(actual.getPhysicalExaminations().size()).isEqualTo(3);
        assertThat(actual.getVisit()).isEqualTo(visit);
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
                new Visit("1", VisitStatus.REGISTERED, LocalDate.now(), patient, registrant, doctor),
                new Visit("2", VisitStatus.CANCELED, LocalDate.now(), patient, registrant, doctor),
                new Visit("3", VisitStatus.FINISHED, LocalDate.now(), patient, registrant, doctor)
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