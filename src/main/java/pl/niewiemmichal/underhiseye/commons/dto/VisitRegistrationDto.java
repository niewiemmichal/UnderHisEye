package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.niewiemmichal.underhiseye.entities.Patient;
import pl.niewiemmichal.underhiseye.entities.VisitStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VisitRegistrationDto {

    @NotNull
    private LocalDateTime date;
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
    @NotNull
    private Long registrantId;
}
