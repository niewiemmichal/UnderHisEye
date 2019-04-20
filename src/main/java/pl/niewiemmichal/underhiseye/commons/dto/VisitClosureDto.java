package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VisitClosureDto {

    @NonNull
    private String description;
    private String diagnosis;
    private List<String> laboratoryExaminationCodes;
    private List<PhysicalExaminationDto> physicalExaminations;

}
