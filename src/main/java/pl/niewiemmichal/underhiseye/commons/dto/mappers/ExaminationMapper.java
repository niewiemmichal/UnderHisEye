package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

@Mapper(componentModel = "spring", uses = { EntityIdMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExaminationMapper {

    ExaminationMapper INSTANCE = Mappers.getMapper(ExaminationMapper.class);

    @Mapping(target = "visit", source = "visitId")
    @Mapping(target = "examination", source = "examinationCode")
    PhysicalExamination toEntity(PhysicalExaminationDto dto);

    @Mapping(target = "visit", source = "visitId")
    @Mapping(target = "examination", source = "examinationCode")
    @Mapping(target = "status", constant = "ORDERED")
    LaboratoryExamination toEntity(LaboratoryExaminationDto dto);

    @Mapping(target = "assistant", source = "assistantId")
    @Mapping(target = "completionDate", expression = "java(java.time.LocalDate.now())")
    LaboratoryExamination toEntity(AssistantClosureDto dto, @MappingTarget LaboratoryExamination examination);

    @Mapping(target = "assistant", source = "supervisorId")
    @Mapping(target = "approvalDate", expression = "java(java.time.LocalDate.now())")
    LaboratoryExamination toEntity(SupervisorClosureDto dto, @MappingTarget LaboratoryExamination examination);
}
