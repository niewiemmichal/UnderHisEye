package pl.niewiemmichal.underhiseye.services;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultExaminationServiceTest {

    @Mock
    private ExaminationRepository examinationRepository;

    @Mock
    private LaboratoryExaminationRepository laboratoryExaminationRepository;

    @Mock
    private PhysicalExaminationRepository physicalExaminationRepository;

    @Mock
    private LaboratoryAssistantRepository laboratoryAssistantRepository;

    @Mock
    private LaboratorySupervisorRepository laboratorySupervisorRepository;

    @InjectMocks
    private DefaultExaminationService examinationService;

    private final Examination EXAMINATION = new Examination("name0");

    private final String NOT_EXISTING_EXAMINATION_CODE = "wrong_code";

    private final Doctor DOCTOR = new Doctor("Existing", "Doctor", "123");
    private final Address PATIENT_ADDRESS = new Address("City", "Street", "HN");

    private final Patient PATIENT = new Patient("Existing", "Patient", "123",
            PATIENT_ADDRESS);

    private final Registrant REGISTRANT =
            new Registrant("Existing", "Registrator");

    private final Visit VISIT =
            new Visit("description", VisitStatus.REGISTERED, LocalDate.of(2019, 12, 20), //
                    PATIENT, REGISTRANT, DOCTOR);

    private final Long NOT_EXISTING_VISIT_ID = 11L;

    private final LaboratoryExamination LABORATORY_EXAMINATION = new LaboratoryExamination( //
            LaboratoryExamStatus.ORDERED, EXAMINATION, VISIT);

    private final PhysicalExamination PHYSICAL_EXAMINATION = new PhysicalExamination("result", EXAMINATION, //
            VISIT);

    private final Long NOT_EXISTING_PHYSICAL_EXAMINATION_ID = 1001L;
    private final Long NOT_EXISTING_LABORATORY_EXAMINATION_ID = 2001L;

    private final LaboratoryAssistant ASSISTANT = new LaboratoryAssistant("name", "surname");
    private final LaboratorySupervisor SUPERVISOR = new LaboratorySupervisor("name", "surname");

    private final AssistantClosureDto ASSISTANT_CLOSURE_DTO = new AssistantClosureDto("result", 100L);
    private final SupervisorClosureDto SUPERVISOR_CLOSURE_DTO = new SupervisorClosureDto("note", 200L);

    private final Long NOT_EXISTING_ASSISTANT_ID = 101L;
    private final Long NOT_EXISTING_SUPERVISOR_ID = 202L;

    PhysicalExaminationDto PHYSICAL_EXAMINATION_DTO = new PhysicalExaminationDto(PHYSICAL_EXAMINATION.getResult(),
            PHYSICAL_EXAMINATION.getExamination().getCode(), VISIT.getId());

    LaboratoryExaminationDto LABORATORY_EXAMINATION_DTO= new LaboratoryExaminationDto(LABORATORY_EXAMINATION.getResult(),
            LABORATORY_EXAMINATION.getExamination().getCode(), VISIT.getId());

    @Before
    public void setUpMocks() {
        initMocks(this);
        VISIT.setId(10L);
        ASSISTANT.setId(100L);
        SUPERVISOR.setId(200L);
        EXAMINATION.setCode("code0");
        LABORATORY_EXAMINATION.setId(1000L);
        PHYSICAL_EXAMINATION.setId(2000L);

        //EXAMINATION REPOSITORY MOCKS
        given(examinationRepository.findById(EXAMINATION.getCode()))
                .willReturn(Optional.of(EXAMINATION));
        given(examinationRepository.findById(NOT_EXISTING_EXAMINATION_CODE)).willReturn(Optional.empty());

        //LABORATORY EXAMINATION REPOSITORY MOCKS
        given(laboratoryExaminationRepository.findById(LABORATORY_EXAMINATION.getId()))
                .willReturn(Optional.of(LABORATORY_EXAMINATION));
        given(laboratoryExaminationRepository.findById(NOT_EXISTING_LABORATORY_EXAMINATION_ID))
                .willReturn(Optional.empty());
        given(laboratoryExaminationRepository.save(LABORATORY_EXAMINATION)).willReturn(LABORATORY_EXAMINATION);

        //PHYSICAL EXAMINATION REPOSITORY MOCKS
        given(physicalExaminationRepository.findById(PHYSICAL_EXAMINATION.getId()))
                .willReturn(Optional.of(PHYSICAL_EXAMINATION));
        given(physicalExaminationRepository.findById(NOT_EXISTING_PHYSICAL_EXAMINATION_ID))
                .willReturn(Optional.empty());
        given(physicalExaminationRepository.save(PHYSICAL_EXAMINATION)).willReturn(PHYSICAL_EXAMINATION);

        //SUPERVISOR REPO MOCKS
        given(laboratorySupervisorRepository.findById(SUPERVISOR.getId())).willReturn(Optional.of(SUPERVISOR));
        given(laboratorySupervisorRepository.findById(NOT_EXISTING_SUPERVISOR_ID)).willReturn(Optional.empty());

        //ASSISTANT REPO MOCKS
        given(laboratoryAssistantRepository.findById(ASSISTANT.getId())).willReturn(Optional.of(ASSISTANT));
        given(laboratoryAssistantRepository.findById(NOT_EXISTING_ASSISTANT_ID)).willReturn(Optional.empty());

    }

    @Test
    public void shouldCreatePhysicalExaminations() {
        //given
        List<PhysicalExaminationDto> physicalExaminations = Lists.newArrayList(PHYSICAL_EXAMINATION_DTO,
                PHYSICAL_EXAMINATION_DTO, PHYSICAL_EXAMINATION_DTO);

        //when
        List<PhysicalExamination> created = examinationService.createPhysicalExaminations(physicalExaminations);

        //then
        PHYSICAL_EXAMINATION.setId(null);
        List<PhysicalExamination> expected = Lists.newArrayList(PHYSICAL_EXAMINATION, PHYSICAL_EXAMINATION,
                PHYSICAL_EXAMINATION);
        assertThat(created).containsExactlyElementsOf(expected);
        verify(physicalExaminationRepository).saveAll(expected);
    }

    @Test
    public void shouldCreateLaboratoryExaminations() {


        //given
        List<LaboratoryExaminationDto> laboratoryExaminations = Lists.newArrayList(LABORATORY_EXAMINATION_DTO,
                LABORATORY_EXAMINATION_DTO, LABORATORY_EXAMINATION_DTO);

        //when
        List<LaboratoryExamination> created = examinationService.createLaboratoryExaminations(laboratoryExaminations);

        //then
        LABORATORY_EXAMINATION.setId(null);
        List<LaboratoryExamination> expected = Lists.newArrayList(LABORATORY_EXAMINATION, LABORATORY_EXAMINATION,
                LABORATORY_EXAMINATION);
        assertThat(created).containsExactlyElementsOf(expected);
        verify(laboratoryExaminationRepository).saveAll(expected);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationCodeIsWrongWhileCreatingPhysicalExaminations() {
        //given
        //when
        PHYSICAL_EXAMINATION_DTO.setExaminationCode(NOT_EXISTING_EXAMINATION_CODE);
        examinationService.createPhysicalExaminations(Lists.newArrayList(PHYSICAL_EXAMINATION_DTO));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationCodeIsWrongWhileCreatingLaboratoryExaminations() {
        //given
        //when
        LABORATORY_EXAMINATION_DTO.setExaminationCode(NOT_EXISTING_EXAMINATION_CODE);
        examinationService.createLaboratoryExaminations(Lists.newArrayList(LABORATORY_EXAMINATION_DTO));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenVisitIdIsWrongWhileCreatingPhysicalExaminations() {
        //given
        //when
        PHYSICAL_EXAMINATION_DTO.setVisitId(NOT_EXISTING_VISIT_ID);
        examinationService.createPhysicalExaminations(Lists.newArrayList(PHYSICAL_EXAMINATION_DTO));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenVisitIdIsWrongWhileCreatingLaboratoryExaminations() {
        //given
        //when
        LABORATORY_EXAMINATION_DTO.setVisitId(NOT_EXISTING_VISIT_ID);
        examinationService.createLaboratoryExaminations(Lists.newArrayList(LABORATORY_EXAMINATION_DTO));
        //then
        //expect exception
    }

    @Test
    public void shouldFinishExamination() {
        //given
        //when
        LaboratoryExamination actual = examinationService.finish(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.FINISHED);
        verify(laboratoryExaminationRepository).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotFinishAndPass() {
        //given
        //when
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        LaboratoryExamination actual = examinationService.finish(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.FINISHED);
        verify(laboratoryExaminationRepository, never()).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotFinishAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.APPROVED, LaboratoryExamStatus.CANCELED,
                LaboratoryExamStatus.REJECTED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                LABORATORY_EXAMINATION.setStatus(status);
                examinationService.finish(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
                fail("Should throw exception when finishing examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileFinishing() {
        //given
        //when
        examinationService.finish(NOT_EXISTING_LABORATORY_EXAMINATION_ID, ASSISTANT_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenAssistantIdIsWrongWhileFinishing() {
        //given
        //when
        ASSISTANT_CLOSURE_DTO.setAssistantId(NOT_EXISTING_ASSISTANT_ID);
        examinationService.finish(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test
    public void shouldCancelExamination() {
        //given
        //when
        LaboratoryExamination actual = examinationService.cancel(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.CANCELED);
        verify(laboratoryExaminationRepository).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotCancelExaminationButPass() {
        //given
        //when
        LaboratoryExamination actual = examinationService.cancel(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.CANCELED);
        verify(laboratoryExaminationRepository, never()).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotCancelAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.APPROVED, LaboratoryExamStatus.REJECTED,
                LaboratoryExamStatus.FINISHED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                LABORATORY_EXAMINATION.setStatus(status);
                examinationService.cancel(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
                fail("Should throw exception when canceling examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileCanceling() {
        //given
        //when
        examinationService.cancel(NOT_EXISTING_LABORATORY_EXAMINATION_ID, ASSISTANT_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenAssistantIdIsWrongWhileCanceling() {
        //given
        //when
        ASSISTANT_CLOSURE_DTO.setAssistantId(NOT_EXISTING_ASSISTANT_ID);
        examinationService.cancel(LABORATORY_EXAMINATION.getId(), ASSISTANT_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test
    public void shouldRejectExamination() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        LaboratoryExamination actual = examinationService.reject(LABORATORY_EXAMINATION.getId(), SUPERVISOR_CLOSURE_DTO);
        //then
        assertThat(actual.getSupervisorNote()).isEqualTo(SUPERVISOR_CLOSURE_DTO.getSupervisorNote());
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.REJECTED);
        verify(laboratoryExaminationRepository).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotRejectButPass() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        LaboratoryExamination actual = examinationService.reject(LABORATORY_EXAMINATION.getId(), SUPERVISOR_CLOSURE_DTO);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.REJECTED);
        verify(laboratoryExaminationRepository, never()).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotRejectAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.ORDERED, LaboratoryExamStatus.APPROVED,
                LaboratoryExamStatus.CANCELED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                LABORATORY_EXAMINATION.setStatus(status);
                examinationService.reject(LABORATORY_EXAMINATION.getId(), SUPERVISOR_CLOSURE_DTO);
                fail("Should throw exception when rejecting examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenSupervisorIdIsWrongWhileRejecting() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        SUPERVISOR_CLOSURE_DTO.setSupervisorId(NOT_EXISTING_SUPERVISOR_ID);
        examinationService.reject(LABORATORY_EXAMINATION.getId(), SUPERVISOR_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileRejecting() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.reject(NOT_EXISTING_LABORATORY_EXAMINATION_ID, SUPERVISOR_CLOSURE_DTO);
        //then
        //expect exception
    }

    @Test
    public void shouldApproveExamination() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        LaboratoryExamination actual = examinationService.approve(LABORATORY_EXAMINATION.getId(),
                SUPERVISOR_CLOSURE_DTO.getSupervisorId());
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.APPROVED);
        verify(laboratoryExaminationRepository).save(LABORATORY_EXAMINATION);
    }

    @Test
    public void shouldNotApproveButPass() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.APPROVED);
        //when
        LaboratoryExamination actual = examinationService.approve(LABORATORY_EXAMINATION.getId(),
                SUPERVISOR_CLOSURE_DTO.getSupervisorId());
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.APPROVED);
        verify(laboratoryExaminationRepository, never()).save(LABORATORY_EXAMINATION);

    }

    @Test(expected = BadRequestException.class)
    public void shouldNotApproveAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.ORDERED, LaboratoryExamStatus.REJECTED,
                LaboratoryExamStatus.CANCELED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                LABORATORY_EXAMINATION.setStatus(status);
                examinationService.approve(LABORATORY_EXAMINATION.getId(), SUPERVISOR_CLOSURE_DTO.getSupervisorId());
                fail("Should throw exception when approving examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileApproving() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.approve(NOT_EXISTING_LABORATORY_EXAMINATION_ID, SUPERVISOR_CLOSURE_DTO.getSupervisorId());
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenSupervisorIdIsWrongWhileApproving() {
        //given
        LABORATORY_EXAMINATION.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.approve(LABORATORY_EXAMINATION.getId(), NOT_EXISTING_SUPERVISOR_ID);
        //then
        //expect exception
    }

    @Test
    public void shouldGetAllLaboratoryExaminations() {
        //given
        List<LaboratoryExamination> laboratoryExaminations = Lists.newArrayList(LABORATORY_EXAMINATION,
                LABORATORY_EXAMINATION, LABORATORY_EXAMINATION);
        given(laboratoryExaminationRepository.findAllByVisit_Id(VISIT.getId())).willReturn(laboratoryExaminations);
        //when
        List<LaboratoryExamination> retrieved = examinationService.getAllLaboratoryExaminationsByVisit(VISIT.getId());
        //then
        assertThat(retrieved).containsExactlyElementsOf(laboratoryExaminations);
    }

    @Test
    public void shouldGetZeroLaboratoryExaminations() {
        //given
        given(laboratoryExaminationRepository.findAllByVisit_Id(NOT_EXISTING_VISIT_ID))
                .willReturn(Lists.newArrayList());
        //when
        List<LaboratoryExamination> retrieved = examinationService
                .getAllLaboratoryExaminationsByVisit(NOT_EXISTING_VISIT_ID);
        //then
        assertThat(retrieved).isEmpty();
    }

    @Test
    public void shouldGetAllPhysicalExaminations() {
        //given
        List<PhysicalExamination> physicalExaminations = Lists.newArrayList(PHYSICAL_EXAMINATION,
                PHYSICAL_EXAMINATION, PHYSICAL_EXAMINATION);
        given(physicalExaminationRepository.findAllByVisit_Id(VISIT.getId())).willReturn(physicalExaminations);
        //when
        List<PhysicalExamination> retrieved = examinationService.getAllPhysicalExaminationsByVisit(VISIT.getId());
        //then
        assertThat(retrieved).containsExactlyElementsOf(physicalExaminations);
    }

    @Test
    public void shouldGetZeroPhysicalExaminations() {
        //given
        given(physicalExaminationRepository.findAllByVisit_Id(NOT_EXISTING_VISIT_ID))
                .willReturn(Lists.newArrayList());
        //when
        List<PhysicalExamination> retrieved = examinationService
                .getAllPhysicalExaminationsByVisit(NOT_EXISTING_VISIT_ID);
        //then
        assertThat(retrieved).isEmpty();
    }

}