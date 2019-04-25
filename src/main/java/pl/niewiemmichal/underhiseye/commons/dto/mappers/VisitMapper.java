package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.entities.Visit.VisitBuilder;

@Component
public class VisitMapper {

    @Autowired
    private EntityIdMapper entityIdMapper;

    public Visit toEntity(VisitRegistrationDto visitRegistrationDto) {
        if ( visitRegistrationDto == null ) {
            return null;
        }

        VisitBuilder visit = Visit.builder();

        visit.doctor( entityIdMapper.toDoctor( visitRegistrationDto.getDoctorId() ) );
        visit.patient( entityIdMapper.toPatient( visitRegistrationDto.getPatientId() ) );
        visit.registrationSpecialist( entityIdMapper.toRegistrant( visitRegistrationDto.getRegistrantId() ) );
        visit.date( visitRegistrationDto.getDate() );

        return visit.build();
    }

    public Visit toEntity(VisitClosureDto visitClosureDto, Visit visit) {
        if ( visitClosureDto == null ) {
            return null;
        }

        visit.setDescription( visitClosureDto.getDescription() );
        visit.setDiagnosis( visitClosureDto.getDiagnosis() );

        return visit;
    }
}
