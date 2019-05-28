package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class PhysicalExaminationDto {
    @NonNull
    private String result;
    @NonNull
    private String examinationCode;
    @NonNull
    private Long visitId;
}
