package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

import java.util.List;

public interface ExaminationService {
    List<PhysicalExamination> createPhysicalExaminations(List<PhysicalExaminationDto> physicalExaminations);
    List<LaboratoryExamination> createLaboratoryExaminations(List<LaboratoryExaminationDto> laboratoryExaminations);
    LaboratoryExamination finish(Long id, AssistantClosureDto assistantClosureDto);
    LaboratoryExamination cancel(Long id, AssistantClosureDto assistantClosureDto);
    LaboratoryExamination reject(Long id, SupervisorClosureDto supervisorClosureDto);
    LaboratoryExamination approve(Long id, Long supervisorId);
    List<LaboratoryExamination> getAllLaboratoryExaminationsByVisit(Long visitId);
    List<PhysicalExamination> getAllPhysicalExaminationsByVisit(Long visitId);
}
