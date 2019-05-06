package pl.niewiemmichal.underhiseye.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.mappers.ExaminationMapper;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamStatus;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.repositories.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DefaultExaminationService implements ExaminationService {


    private final ExaminationRepository examinationRepository;
    private final LaboratoryExaminationRepository laboratoryExaminationRepository;
    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final LaboratoryAssistantRepository laboratoryAssistantRepository;
    private final LaboratorySupervisorRepository laboratorySupervisorRepository;
    private final VisitRepository visitRepository;
    private final ExaminationMapper examinationMapper;

    @Autowired
    public DefaultExaminationService(ExaminationRepository examinationRepository,
                                     LaboratoryExaminationRepository laboratoryExaminationRepository,
                                     PhysicalExaminationRepository physicalExaminationRepository,
                                     LaboratoryAssistantRepository laboratoryAssistantRepository,
                                     LaboratorySupervisorRepository laboratorySupervisorRepository,
                                     VisitRepository visitRepository,
                                     ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.laboratoryExaminationRepository = laboratoryExaminationRepository;
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.examinationMapper = examinationMapper;
        this.visitRepository = visitRepository;
    }

    @Override
    public List<PhysicalExamination> createPhysicalExaminations(@NonNull List<PhysicalExaminationDto> physicalExaminations) {
        return physicalExaminationRepository.saveAll(physicalExaminations.stream()
                .map(e -> wrapToEntity(() -> examinationMapper.toEntity(e)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<LaboratoryExamination> createLaboratoryExaminations(@NonNull List<LaboratoryExaminationDto> laboratoryExaminations) {
        return laboratoryExaminationRepository.saveAll(laboratoryExaminations.stream()
                .map(e -> wrapToEntity(() -> examinationMapper.toEntity(e)))
                .collect(Collectors.toList()));
    }

    @Override
    public LaboratoryExamination finish(@NonNull Long id, @NonNull AssistantClosureDto assistantClosureDto) {
        return changeState(LaboratoryExamStatus.FINISHED, LaboratoryExamStatus.ORDERED,
                () -> examinationMapper.toEntity(assistantClosureDto, findExamination(id)));
    }

    @Override
    public LaboratoryExamination cancel(@NonNull Long id, @NonNull AssistantClosureDto assistantClosureDto) {
        return changeState(LaboratoryExamStatus.CANCELED, LaboratoryExamStatus.ORDERED,
                () -> examinationMapper.toEntity(assistantClosureDto, findExamination(id)));
    }

    @Override
    public LaboratoryExamination reject(@NonNull Long id, @NonNull SupervisorClosureDto supervisorClosureDto) {
        return changeState(LaboratoryExamStatus.REJECTED, LaboratoryExamStatus.FINISHED,
                () -> examinationMapper.toEntity(supervisorClosureDto, findExamination(id)));
    }

    @Override
    public LaboratoryExamination approve(@NonNull Long id,@NonNull Long supervisorId) {
        return changeState(LaboratoryExamStatus.APPROVED, LaboratoryExamStatus.FINISHED,
                () -> examinationMapper.toEntity(supervisorId, findExamination(id)));
    }

    @Override
    public List<LaboratoryExamination> getAllLaboratoryExaminationsByVisit(@NonNull Long visitId) {
        return laboratoryExaminationRepository.findAllByVisit_Id(visitId);
    }

    @Override
    public List<PhysicalExamination> getAllPhysicalExaminationsByVisit(@NonNull Long visitId) {
        return physicalExaminationRepository.findAllByVisit_Id(visitId);
    }

    private LaboratoryExamination changeState(LaboratoryExamStatus newState, LaboratoryExamStatus currentState,
                                                        Supplier<LaboratoryExamination> toEntity) {
        LaboratoryExamination laboratoryExamination = wrapToEntity(toEntity);

        if(laboratoryExamination.getStatus() != newState) {
            if(laboratoryExamination.getStatus() != currentState)
                throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), "should equal " + currentState.toString());
            laboratoryExamination.setStatus(newState);
            laboratoryExaminationRepository.save(laboratoryExamination);
        }

        return laboratoryExamination;
    }

    private <T> T wrapToEntity(Supplier<T> toEntity) {
        try {
            return toEntity.get();
        } catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    private LaboratoryExamination findExamination(Long id) {
        return laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new BadRequestException("LaboratoryExamination", "id", id.toString(), " does not exist"));
    }
}
