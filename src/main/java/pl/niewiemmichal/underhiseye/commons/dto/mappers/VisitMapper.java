package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.entities.Visit;

@Mapper(componentModel = "spring", uses = { EntityIdMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {

    VisitMapper INSTANCE = Mappers.getMapper( VisitMapper.class );

    @Mapping(target = "patient", source = "patientId")
    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "registrationSpecialist", source = "registrantId")
    Visit toEntity(VisitRegistrationDto visitRegistrationDto);
}
