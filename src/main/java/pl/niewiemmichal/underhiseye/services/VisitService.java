package pl.niewiemmichal.underhiseye.services;

import pl.niewiemmichal.underhiseye.model.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.model.PhysicalExamination;
import pl.niewiemmichal.underhiseye.model.Visit;

import java.util.List;

public interface VisitService {

    void cancel(Long visitId);
    Visit register(Visit visit);
    Visit end(Long visitId, String description, String note, List<LaboratoryExamination> laboratoryExaminations,
              List<PhysicalExamination> physicalExaminations);

}
