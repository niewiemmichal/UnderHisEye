package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class PhysicalExaminationDto {
    @NonNull
    private String result;
    @NonNull
    private String examinationCode;
}
