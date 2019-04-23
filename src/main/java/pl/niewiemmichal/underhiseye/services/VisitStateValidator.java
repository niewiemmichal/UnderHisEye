package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.entities.VisitStatus;

public class VisitStateValidator {
    public boolean cancelableVisit(VisitStatus visitStatus) {
        return visitStatus == VisitStatus.REGISTERED;
    }

    public boolean finishableVisit(VisitStatus visitStatus) {
        return visitStatus == VisitStatus.REGISTERED;
    }
}
