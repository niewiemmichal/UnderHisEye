package pl.niewiemmichal.underhiseye.services;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultExaminationServiceTest {

    @MockBean
    private ExaminationRepository examinationRepository;

    @MockBean
    private LaboratoryExaminationRepository laboratoryExaminationRepository;

    @MockBean
    private PhysicalExaminationRepository physicalExaminationRepository;

    @MockBean
    private LaboratoryAssistantRepository laboratoryAssistantRepository;

    @MockBean
    private LaboratorySupervisorRepository laboratorySupervisorRepository;

    @MockBean
    private VisitRepository visitRepository;

    @Autowired
    private DefaultExaminationService examinationService;

    private static final String NOT_EXISTING_EXAMINATION_CODE = "wrong_code";
    private static final Long NOT_EXISTING_VISIT_ID = 11L;
    private static final Long NOT_EXISTING_PHYSICAL_EXAMINATION_ID = 1001L;
    private static final Long NOT_EXISTING_LABORATORY_EXAMINATION_ID = 2001L;
    private static final Long NOT_EXISTING_ASSISTANT_ID = 101L;
    private static final Long NOT_EXISTING_SUPERVISOR_ID = 202L;

    private Examination examination = new Examination("code0", "name0");
    private Doctor doctor = new Doctor("Existing", "Doctor", "123");
    private Address patientAddress = new Address("City", "Street", "HN");
    private Patient patient = new Patient("Existing", "Patient", "123", patientAddress);
    private Registrant registrant = new Registrant("Existing", "Registrant");

    private Visit visit = new Visit("description", VisitStatus.REGISTERED,
            LocalDate.of(2019, 12, 20), patient, registrant, doctor);

    private LaboratoryExamination laboratoryExamination = new LaboratoryExamination(LaboratoryExamStatus.ORDERED,
            examination, visit);

    private PhysicalExamination physicalExamination = new PhysicalExamination("result", examination, visit);
    private LaboratoryAssistant assistant = new LaboratoryAssistant("name", "surname");
    private LaboratorySupervisor supervisor = new LaboratorySupervisor("name", "surname");
    private AssistantClosureDto assistantClosureDto = new AssistantClosureDto("result", 100L);
    private SupervisorClosureDto supervisorClosureDto = new SupervisorClosureDto(200L);

    private PhysicalExaminationDto physicalExaminationDto;

    private LaboratoryExaminationDto laboratoryExaminationDto;

    @Before
    public void setUpMocks() {
        initMocks(this);
        visit.setId(10L);
        assistant.setId(100L);
        supervisor.setId(200L);
        laboratoryExamination.setId(1000L);
        physicalExamination.setId(2000L);
        supervisorClosureDto.setNote("note");

        physicalExaminationDto = new PhysicalExaminationDto(physicalExamination.getResult(),
                physicalExamination.getExamination().getCode(), visit.getId());

        laboratoryExaminationDto = new LaboratoryExaminationDto(laboratoryExamination.getExamination().getCode(), visit.getId());
        laboratoryExaminationDto.setNote("note");

        //visit REPOSITORY MOCKS
        given(visitRepository.findById(visit.getId())).willReturn(Optional.of(visit));
        given(visitRepository.findById(NOT_EXISTING_VISIT_ID)).willReturn(Optional.empty());
        given(visitRepository.save(visit)).willReturn(visit);

        //examination REPOSITORY MOCKS
        given(examinationRepository.findById(examination.getCode()))
                .willReturn(Optional.of(examination));
        given(examinationRepository.findById(NOT_EXISTING_EXAMINATION_CODE)).willReturn(Optional.empty());

        //LABORATORY examination REPOSITORY MOCKS
        given(laboratoryExaminationRepository.findById(laboratoryExamination.getId()))
                .willReturn(Optional.of(laboratoryExamination));
        given(laboratoryExaminationRepository.findById(NOT_EXISTING_LABORATORY_EXAMINATION_ID))
                .willReturn(Optional.empty());
        given(laboratoryExaminationRepository.save(laboratoryExamination)).willReturn(laboratoryExamination);

        //PHYSICAL examination REPOSITORY MOCKS
        given(physicalExaminationRepository.findById(physicalExamination.getId()))
                .willReturn(Optional.of(physicalExamination));
        given(physicalExaminationRepository.findById(NOT_EXISTING_PHYSICAL_EXAMINATION_ID))
                .willReturn(Optional.empty());
        given(physicalExaminationRepository.save(physicalExamination)).willReturn(physicalExamination);

        //supervisor REPO MOCKS
        given(laboratorySupervisorRepository.findById(supervisor.getId())).willReturn(Optional.of(supervisor));
        given(laboratorySupervisorRepository.findById(NOT_EXISTING_SUPERVISOR_ID)).willReturn(Optional.empty());

        //assistant REPO MOCKS
        given(laboratoryAssistantRepository.findById(assistant.getId())).willReturn(Optional.of(assistant));
        given(laboratoryAssistantRepository.findById(NOT_EXISTING_ASSISTANT_ID)).willReturn(Optional.empty());
    }

    @Test
    public void shouldCreatePhysicalExaminations() {
        //given
        List<PhysicalExaminationDto> physicalExaminations = Lists.newArrayList(physicalExaminationDto,
                physicalExaminationDto, physicalExaminationDto);

        physicalExamination.setId(null);
        List<PhysicalExamination> expected = Lists.newArrayList(physicalExamination, physicalExamination,
                physicalExamination);
        given(physicalExaminationRepository.saveAll(expected)).willReturn(expected);

        //when
        List<PhysicalExamination> created = examinationService.createPhysicalExaminations(physicalExaminations);

        //then
        assertThat(created).containsExactlyElementsOf(expected);
        verify(physicalExaminationRepository).saveAll(expected);
    }

    @Test
    public void shouldCreateLaboratoryExaminations() {
        //given
        List<LaboratoryExaminationDto> laboratoryExaminations = Lists.newArrayList(laboratoryExaminationDto,
                laboratoryExaminationDto, laboratoryExaminationDto);

        laboratoryExamination.setNote("note");
        laboratoryExamination.setId(null);
        List<LaboratoryExamination> expected = Lists.newArrayList(laboratoryExamination, laboratoryExamination,
                laboratoryExamination);
        given(laboratoryExaminationRepository.saveAll(expected)).willReturn(expected);

        //when
        List<LaboratoryExamination> created = examinationService.createLaboratoryExaminations(laboratoryExaminations);

        //then
        assertThat(created).containsExactlyElementsOf(expected);
        verify(laboratoryExaminationRepository).saveAll(expected);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationCodeIsWrongWhileCreatingPhysicalExaminations() {
        //given
        //when
        physicalExaminationDto.setExaminationCode(NOT_EXISTING_EXAMINATION_CODE);
        examinationService.createPhysicalExaminations(Lists.newArrayList(physicalExaminationDto));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationCodeIsWrongWhileCreatingLaboratoryExaminations() {
        //given
        //when
        laboratoryExaminationDto.setExaminationCode(NOT_EXISTING_EXAMINATION_CODE);
        examinationService.createLaboratoryExaminations(Lists.newArrayList(laboratoryExaminationDto));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenVisitIdIsWrongWhileCreatingPhysicalExaminations() {
        //given
        //when
        physicalExaminationDto.setVisitId(NOT_EXISTING_VISIT_ID);
        examinationService.createPhysicalExaminations(Lists.newArrayList(physicalExaminationDto));
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenVisitIdIsWrongWhileCreatingLaboratoryExaminations() {
        //given
        //when
        laboratoryExaminationDto.setVisitId(NOT_EXISTING_VISIT_ID);
        examinationService.createLaboratoryExaminations(Lists.newArrayList(laboratoryExaminationDto));
        //then
        //expect exception
    }

    @Test
    public void shouldFinishExamination() {
        //given
        //when
        LaboratoryExamination actual = examinationService.finish(laboratoryExamination.getId(), assistantClosureDto);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.FINISHED);
        verify(laboratoryExaminationRepository).save(laboratoryExamination);
    }

    @Test
    public void shouldNotFinishAndPass() {
        //given
        //when
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        LaboratoryExamination actual = examinationService.finish(laboratoryExamination.getId(), assistantClosureDto);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.FINISHED);
        verify(laboratoryExaminationRepository, never()).save(laboratoryExamination);
    }

    @Test
    public void shouldNotFinishAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.APPROVED, LaboratoryExamStatus.CANCELED,
                LaboratoryExamStatus.REJECTED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                laboratoryExamination.setStatus(status);
                examinationService.finish(laboratoryExamination.getId(), assistantClosureDto);
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
        examinationService.finish(NOT_EXISTING_LABORATORY_EXAMINATION_ID, assistantClosureDto);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenAssistantIdIsWrongWhileFinishing() {
        //given
        //when
        assistantClosureDto.setAssistantId(NOT_EXISTING_ASSISTANT_ID);
        examinationService.finish(laboratoryExamination.getId(), assistantClosureDto);
        //then
        //expect exception
    }

    @Test
    public void shouldCancelExamination() {
        //given
        //when
        LaboratoryExamination actual = examinationService.cancel(laboratoryExamination.getId(), assistantClosureDto);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.CANCELED);
        verify(laboratoryExaminationRepository).save(laboratoryExamination);
    }

    @Test
    public void shouldNotCancelExaminationButPass() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.CANCELED);
        //when
        LaboratoryExamination actual = examinationService.cancel(laboratoryExamination.getId(), assistantClosureDto);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.CANCELED);
        verify(laboratoryExaminationRepository, never()).save(laboratoryExamination);
    }

    @Test
    public void shouldNotCancelAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.APPROVED, LaboratoryExamStatus.REJECTED,
                LaboratoryExamStatus.FINISHED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                laboratoryExamination.setStatus(status);
                examinationService.cancel(laboratoryExamination.getId(), assistantClosureDto);
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
        examinationService.cancel(NOT_EXISTING_LABORATORY_EXAMINATION_ID, assistantClosureDto);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenAssistantIdIsWrongWhileCanceling() {
        //given
        //when
        assistantClosureDto.setAssistantId(NOT_EXISTING_ASSISTANT_ID);
        examinationService.cancel(laboratoryExamination.getId(), assistantClosureDto);
        //then
        //expect exception
    }

    @Test
    public void shouldRejectExamination() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        LaboratoryExamination actual = examinationService.reject(laboratoryExamination.getId(), supervisorClosureDto);
        //then
        assertThat(actual.getSupervisorNote()).isEqualTo(supervisorClosureDto.getNote());
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.REJECTED);
        verify(laboratoryExaminationRepository).save(laboratoryExamination);
    }

    @Test
    public void shouldNotRejectButPass() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.REJECTED);
        //when
        LaboratoryExamination actual = examinationService.reject(laboratoryExamination.getId(), supervisorClosureDto);
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.REJECTED);
        verify(laboratoryExaminationRepository, never()).save(laboratoryExamination);
    }

    @Test
    public void shouldNotRejectAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.ORDERED, LaboratoryExamStatus.APPROVED,
                LaboratoryExamStatus.CANCELED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                laboratoryExamination.setStatus(status);
                examinationService.reject(laboratoryExamination.getId(), supervisorClosureDto);
                fail("Should throw exception when rejecting examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenSupervisorIdIsWrongWhileRejecting() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        supervisorClosureDto.setId(NOT_EXISTING_SUPERVISOR_ID);
        examinationService.reject(laboratoryExamination.getId(), supervisorClosureDto);
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileRejecting() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.reject(NOT_EXISTING_LABORATORY_EXAMINATION_ID, supervisorClosureDto);
        //then
        //expect exception
    }

    @Test
    public void shouldApproveExamination() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        LaboratoryExamination actual = examinationService.approve(laboratoryExamination.getId(),
                supervisorClosureDto.getId());
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.APPROVED);
        verify(laboratoryExaminationRepository).save(laboratoryExamination);
    }

    @Test
    public void shouldNotApproveButPass() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.APPROVED);
        //when
        LaboratoryExamination actual = examinationService.approve(laboratoryExamination.getId(),
                supervisorClosureDto.getId());
        //then
        assertThat(actual.getStatus()).isEqualTo(LaboratoryExamStatus.APPROVED);
        verify(laboratoryExaminationRepository, never()).save(laboratoryExamination);

    }

    @Test
    public void shouldNotApproveAndThrow() {
        LaboratoryExamStatus statuses[] = {LaboratoryExamStatus.ORDERED, LaboratoryExamStatus.REJECTED,
                LaboratoryExamStatus.CANCELED};

        for (LaboratoryExamStatus status : statuses) {
            try {
                laboratoryExamination.setStatus(status);
                examinationService.approve(laboratoryExamination.getId(), supervisorClosureDto.getId());
                fail("Should throw exception when approving examination with wrong status");
            } catch (BadRequestException e) {
                //expect exception
            }
        }
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenExaminationIdIsWrongWhileApproving() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.approve(NOT_EXISTING_LABORATORY_EXAMINATION_ID, supervisorClosureDto.getId());
        //then
        //expect exception
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowWhenSupervisorIdIsWrongWhileApproving() {
        //given
        laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
        //when
        examinationService.approve(laboratoryExamination.getId(), NOT_EXISTING_SUPERVISOR_ID);
        //then
        //expect exception
    }

    @Test
    public void shouldGetAllLaboratoryExaminations() {
        //given
        List<LaboratoryExamination> laboratoryExaminations = Lists.newArrayList(laboratoryExamination,
                laboratoryExamination, laboratoryExamination);
        given(laboratoryExaminationRepository.findAllByVisit_Id(visit.getId())).willReturn(laboratoryExaminations);
        //when
        List<LaboratoryExamination> retrieved = examinationService.getAllLaboratoryExaminationsByVisit(visit.getId());
        //then
        assertThat(retrieved).containsExactlyElementsOf(laboratoryExaminations);
    }

    @Test
    public void shouldGetZeroLaboratoryExaminations() {
        //given
        given(laboratoryExaminationRepository.findAllByVisit_Id(NOT_EXISTING_VISIT_ID)).willReturn(Lists.newArrayList());
        //when
        List<LaboratoryExamination> retrieved = examinationService.getAllLaboratoryExaminationsByVisit(NOT_EXISTING_VISIT_ID);
        //then
        assertThat(retrieved).isEmpty();
    }

    @Test
    public void shouldGetAllPhysicalExaminations() {
        //given
        List<PhysicalExamination> physicalExaminations = Lists.newArrayList(physicalExamination, physicalExamination,
                physicalExamination);
        given(physicalExaminationRepository.findAllByVisit_Id(visit.getId())).willReturn(physicalExaminations);
        //when
        List<PhysicalExamination> retrieved = examinationService.getAllPhysicalExaminationsByVisit(visit.getId());
        //then
        assertThat(retrieved).containsExactlyElementsOf(physicalExaminations);
    }

    @Test
    public void shouldGetZeroPhysicalExaminations() {
        //given
        given(physicalExaminationRepository.findAllByVisit_Id(NOT_EXISTING_VISIT_ID)).willReturn(Lists.newArrayList());
        //when
        List<PhysicalExamination> retrieved = examinationService.getAllPhysicalExaminationsByVisit(NOT_EXISTING_VISIT_ID);
        //then
        assertThat(retrieved).isEmpty();
    }

}