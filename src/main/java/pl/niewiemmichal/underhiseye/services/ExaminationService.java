package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

import java.util.List;

public interface ExaminationService {
    List<PhysicalExamination> createPhysicalExaminations(List<PhysicalExaminationDto> physicalExaminations);
    List<LaboratoryExamination> createLaboratoryExaminations(List<String> laboratoryExaminationCodes);
}
