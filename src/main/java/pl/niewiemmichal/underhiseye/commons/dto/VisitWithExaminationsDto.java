package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.*;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;
import pl.niewiemmichal.underhiseye.entities.Visit;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class VisitWithExaminationsDto {
    @NonNull
    private final Visit visit;
    @NonNull
    private final List<LaboratoryExamination> laboratoryExaminations;
    @NonNull
    private final List<PhysicalExamination> physicalExaminations;
}
