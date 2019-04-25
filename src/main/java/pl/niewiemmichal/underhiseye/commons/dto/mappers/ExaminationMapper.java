package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamStatus;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination.LaboratoryExaminationBuilder;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination.PhysicalExaminationBuilder;

@Component
public class ExaminationMapper {

    @Autowired
    private EntityIdMapper entityIdMapper;

    public PhysicalExamination toEntity(PhysicalExaminationDto dto) {
        if ( dto == null ) {
            return null;
        }

        PhysicalExaminationBuilder physicalExamination = PhysicalExamination.builder();

        physicalExamination.examination( entityIdMapper.toExamination( dto.getExaminationCode() ) );
        physicalExamination.visit( entityIdMapper.toVisit( dto.getVisitId() ) );
        physicalExamination.result( dto.getResult() );

        return physicalExamination.build();
    }

    public LaboratoryExamination toEntity(LaboratoryExaminationDto dto) {
        if ( dto == null ) {
            return null;
        }

        LaboratoryExaminationBuilder laboratoryExamination = LaboratoryExamination.builder();

        laboratoryExamination.visit( entityIdMapper.toVisit( dto.getVisitId() ) );
        laboratoryExamination.examination( entityIdMapper.toExamination( dto.getExaminationCode() ) );
        laboratoryExamination.note( dto.getNote() );

        laboratoryExamination.status( LaboratoryExamStatus.ORDERED );

        return laboratoryExamination.build();
    }

    public LaboratoryExamination toEntity(AssistantClosureDto dto, LaboratoryExamination examination) {
        if ( dto == null ) {
            return null;
        }

        examination.setAssistant( entityIdMapper.toAssistant( dto.getAssistantId() ) );
        examination.setResult( dto.getResult() );

        examination.setCompletionDate( java.time.LocalDate.now() );

        return examination;
    }

    public LaboratoryExamination toEntity(SupervisorClosureDto dto, LaboratoryExamination examination) {
        if ( dto == null ) {
            return null;
        }

        examination.setSupervisor( entityIdMapper.toSupervisor( dto.getSupervisorId() ) );
        examination.setSupervisorNote( dto.getSupervisorNote() );

        examination.setApprovalDate( java.time.LocalDate.now() );

        return examination;
    }
}
