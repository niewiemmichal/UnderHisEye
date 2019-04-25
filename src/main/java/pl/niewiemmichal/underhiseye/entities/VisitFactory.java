package pl.niewiemmichal.underhiseye.entities;

import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.repositories.VisitRepository;


@Component
public class VisitFactory {

    private VisitRepository visitRepository;

    @Autowired
    private VisitFactory(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @ObjectFactory
    public Visit createVisit() {
        return new Visit();
    }

    @ObjectFactory
    public Visit getVisit(Long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
    }

}
