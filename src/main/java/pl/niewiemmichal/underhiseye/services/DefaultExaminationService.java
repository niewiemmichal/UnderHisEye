package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.mappers.ExaminationMapper;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.*;

import java.util.List;

@Service
public class DefaultExaminationService implements ExaminationService {


    private final ExaminationRepository examinationRepository;
    private final LaboratoryExaminationRepository laboratoryExaminationRepository;
    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final LaboratoryAssistantRepository laboratoryAssistantRepository;
    private final LaboratorySupervisorRepository laboratorySupervisorRepository;
    private final ExaminationMapper examinationMapper;

    @Autowired
    public DefaultExaminationService(ExaminationRepository examinationRepository,
                                     LaboratoryExaminationRepository laboratoryExaminationRepository,
                                     PhysicalExaminationRepository physicalExaminationRepository,
                                     LaboratoryAssistantRepository laboratoryAssistantRepository,
                                     LaboratorySupervisorRepository laboratorySupervisorRepository,
                                     ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.laboratoryExaminationRepository = laboratoryExaminationRepository;
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.examinationMapper = examinationMapper;
    }

    @Override
    public List<PhysicalExamination> createPhysicalExaminations(List<PhysicalExaminationDto> physicalExaminations) {
        return null;
    }

    @Override
    public List<LaboratoryExamination> createLaboratoryExaminations(List<LaboratoryExaminationDto> laboratoryExaminations) {
        return null;
    }

    @Override
    public LaboratoryExamination finish(Long id, AssistantClosureDto assistantClosureDto) {
        return null;
    }

    @Override
    public LaboratoryExamination cancel(Long id, AssistantClosureDto assistantClosureDto) {
        return null;
    }

    @Override
    public LaboratoryExamination reject(Long id, SupervisorClosureDto supervisorClosureDto) {
        return null;
    }

    @Override
    public LaboratoryExamination approve(Long id, Long supervisorId) {
        return null;
    }

    @Override
    public List<LaboratoryExamination> getAllLaboratoryExaminationsByVisit(Long visitId) {
        return null;
    }

    @Override
    public List<PhysicalExamination> getAllPhysicalExaminationsByVisit(Long visitId) {
        return null;
    }
}
