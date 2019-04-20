package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VisitRegistrationDto {

    @NotNull
    private LocalDate date;
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
    @NotNull
    private Long registrantId;

}
