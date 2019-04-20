package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;

@Mapper(componentModel = "spring", uses = { EntityIdMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExaminationMapper {

    @Mapping(target = "visit", source = "visitId")
    PhysicalExamination toEntity(PhysicalExaminationDto dto, Integer visitId);

}
