package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import java.util.List;

public interface VisitService {

    void cancel(Long visitId, String reason);
    Visit register(VisitRegistrationDto visitRegistration);
    void end(Long visitId, VisitClosureDto visitClosure);
    Visit get(Long id);
    VisitWithExaminationsDto getFatVisit(Long id);
    List<Visit> getAll();
}
