package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.entities.VisitStatus;

@Mapper(componentModel = "spring", uses = { EntityIdMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    @Mapping(target = "patient", source = "visitRegistrationDto.patientId")
    @Mapping(target = "doctor", source = "visitRegistrationDto.doctorId")
    @Mapping(target = "registrationSpecialist", source = "visitRegistrationDto.registrantId")
    @Mapping(target = "status", constant = "REGISTERED")
    Visit toEntity(VisitRegistrationDto visitRegistrationDto);


    @Mapping(target = "description", source = "visitClosureDto.description")
    @Mapping(target = "diagnosis", source = "visitClosureDto.diagnosis")
    Visit toEntity(VisitClosureDto visitClosureDto, @MappingTarget Visit visit);
}