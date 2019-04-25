package pl.niewiemmichal.underhiseye.commons.dto;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class LaboratoryExaminationDto {
    private String note;
    @NonNull
    private String examinationCode;
    @NonNull
    private Long visitId;
}
