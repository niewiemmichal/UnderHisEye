package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.entities.Visit;

public interface VisitService {

    void cancel(Long visitId, String reason);
    Visit register(VisitRegistrationDto visitRegistration);
    void end(Long visitId, VisitClosureDto visitClosure);

}
